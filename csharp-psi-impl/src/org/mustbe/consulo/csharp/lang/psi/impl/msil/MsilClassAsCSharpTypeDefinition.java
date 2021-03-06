/*
 * Copyright 2013-2014 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mustbe.consulo.csharp.lang.psi.impl.msil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.consulo.lombok.annotations.LazyInstance;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.csharp.lang.psi.CSharpGenericConstraint;
import org.mustbe.consulo.csharp.lang.psi.CSharpGenericConstraintList;
import org.mustbe.consulo.csharp.lang.psi.CSharpTypeDeclaration;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpTypeDeclarationImplUtil;
import org.mustbe.consulo.dotnet.DotNetTypes;
import org.mustbe.consulo.dotnet.psi.*;
import org.mustbe.consulo.dotnet.resolve.DotNetTypeRef;
import org.mustbe.consulo.msil.MsilHelper;
import org.mustbe.consulo.msil.lang.psi.MsilClassEntry;
import org.mustbe.consulo.msil.lang.psi.MsilEventEntry;
import org.mustbe.consulo.msil.lang.psi.MsilFieldEntry;
import org.mustbe.consulo.msil.lang.psi.MsilMethodEntry;
import org.mustbe.consulo.msil.lang.psi.MsilModifierList;
import org.mustbe.consulo.msil.lang.psi.MsilPropertyEntry;
import org.mustbe.consulo.msil.lang.psi.MsilTokens;
import org.mustbe.consulo.msil.lang.psi.MsilXXXAcessor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;

/**
 * @author VISTALL
 * @since 22.05.14
 */
public class MsilClassAsCSharpTypeDefinition extends MsilElementWrapper<MsilClassEntry> implements CSharpTypeDeclaration
{
	private NotNullLazyValue<DotNetNamedElement[]> myMembersValue = new NotNullLazyValue<DotNetNamedElement[]>()
	{
		@NotNull
		@Override
		protected DotNetNamedElement[] compute()
		{
			MsilClassAsCSharpTypeDefinition parentThis = MsilClassAsCSharpTypeDefinition.this;

			DotNetNamedElement[] temp = myMsilElement.getMembers();
			List<DotNetNamedElement> copy = new ArrayList<DotNetNamedElement>(temp.length);
			Collections.addAll(copy, temp);

			List<DotNetNamedElement> list = new ArrayList<DotNetNamedElement>(temp.length);

			boolean isEnum = isEnum();
			List<String> bannedFieldNames = new ArrayList<String>();
			for(DotNetNamedElement element : temp)
			{
				if(element instanceof MsilFieldEntry)
				{
					String name = element.getName();
					if(name == null)
					{
						continue;
					}

					if(StringUtil.contains(name, "<>") || Comparing.equal(name, "value__") && isEnum)
					{
						bannedFieldNames.add(name);
					}
				}
				else if(element instanceof MsilEventEntry)
				{
					bannedFieldNames.add(element.getName());
				}
			}

			for(DotNetNamedElement element : temp)
			{
				if(element instanceof MsilPropertyEntry)
				{
					DotNetXXXAccessor[] accessors = ((MsilPropertyEntry) element).getAccessors();

					List<Pair<DotNetXXXAccessor, MsilMethodEntry>> pairs = new ArrayList<Pair<DotNetXXXAccessor, MsilMethodEntry>>(2);

					for(DotNetXXXAccessor accessor : accessors)
					{
						MsilMethodEntry methodEntry = findMethodEntry(temp, (MsilXXXAcessor) accessor);
						if(methodEntry != null)
						{
							pairs.add(Pair.create(accessor, methodEntry));
							copy.remove(methodEntry);
						}
					}

					if(!pairs.isEmpty())
					{
						Pair<DotNetXXXAccessor, MsilMethodEntry> value = pairs.get(0);

						if(value.getFirst().getAccessorKind() == DotNetXXXAccessor.Kind.GET && value.getSecond().getParameters().length == 1 ||
								value.getFirst().getAccessorKind() == DotNetXXXAccessor.Kind.SET && value.getSecond().getParameters().length == 2)
						{
							list.add(new MsilPropertyAsCSharpArrayMethodDeclaration(parentThis, (MsilPropertyEntry) element, pairs));
							continue;
						}
					}

					list.add(new MsilPropertyAsCSharpPropertyDeclaration(parentThis, (MsilPropertyEntry) element, pairs));
				}
				else if(element instanceof MsilEventEntry)
				{
					DotNetXXXAccessor[] accessors = ((MsilEventEntry) element).getAccessors();

					List<Pair<DotNetXXXAccessor, MsilMethodEntry>> pairs = new ArrayList<Pair<DotNetXXXAccessor, MsilMethodEntry>>(2);

					for(DotNetXXXAccessor accessor : accessors)
					{
						MsilMethodEntry methodEntry = findMethodEntry(temp, (MsilXXXAcessor) accessor);
						if(methodEntry != null)
						{
							pairs.add(Pair.create(accessor, methodEntry));
							copy.remove(methodEntry);
						}
					}
					list.add(new MsilEventAsCSharpEventDeclaration(parentThis, (MsilEventEntry) element, pairs));
				}
				else if(element instanceof MsilFieldEntry)
				{
					String name = element.getName();
					if(bannedFieldNames.contains(name))
					{
						continue;
					}

					if(isEnum)
					{
						list.add(new MsilFieldAsCSharpEnumConstantDeclaration(parentThis, (DotNetVariable) element));
					}
					else
					{
						list.add(new MsilFieldAsCSharpFieldDeclaration(parentThis, (DotNetVariable) element));
					}
				}
				else if(element instanceof MsilClassEntry)
				{
					list.add(new MsilClassAsCSharpTypeDefinition(parentThis, (MsilClassEntry) element));
				}
			}

			for(DotNetNamedElement member : copy)
			{
				if(member instanceof MsilMethodEntry)
				{
					String nameFromBytecode = ((MsilMethodEntry) member).getNameFromBytecode();
					if(Comparing.equal(nameFromBytecode, MsilHelper.STATIC_CONSTRUCTOR_NAME) || StringUtil.startsWith(nameFromBytecode, "<"))
					{
						continue;
					}
					if(MsilHelper.CONSTRUCTOR_NAME.equals(nameFromBytecode))
					{
						list.add(new MsilMethodAsCSharpConstructorDeclaration(parentThis, MsilClassAsCSharpTypeDefinition.this,
								(MsilMethodEntry) member));
					}
					else if(Comparing.equal(nameFromBytecode, "op_Implicit") || Comparing.equal(nameFromBytecode, "op_Explicit"))
					{
						list.add(new MsilMethodAsCSharpConversionMethodDeclaration(parentThis, (MsilMethodEntry) member));
					}
					else
					{
						list.add(new MsilMethodAsCSharpMethodDeclaration(parentThis, null, (MsilMethodEntry) member));
					}
				}
			}
			return list.isEmpty() ? DotNetNamedElement.EMPTY_ARRAY : list.toArray(new DotNetNamedElement[list.size()]);
		}

		private MsilMethodEntry findMethodEntry(DotNetNamedElement[] dotNetNamedElements, MsilXXXAcessor accessor)
		{
			for(DotNetNamedElement element : dotNetNamedElements)
			{
				if(element instanceof MsilMethodEntry && ((MsilMethodEntry) element).hasModifier(MsilTokens.SPECIALNAME_KEYWORD))
				{
					String originalMethodName = StringUtil.unquoteString(((MsilMethodEntry) element).getNameFromBytecode());
					if(Comparing.equal(originalMethodName, accessor.getMethodName()))
					{
						return (MsilMethodEntry) element;
					}
				}
			}
			return null;
		}
	};

	private MsilModifierListToCSharpModifierList myModifierList;

	public MsilClassAsCSharpTypeDefinition(@Nullable PsiElement parent, MsilClassEntry classEntry)
	{
		super(parent, classEntry);
		myModifierList = new MsilModifierListToCSharpModifierList((MsilModifierList) classEntry.getModifierList());
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CSharpElementVisitor)
		{
			((CSharpElementVisitor) visitor).visitTypeDeclaration(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public PsiFile getContainingFile()
	{
		return myMsilElement.getContainingFile();
	}

	@Override
	public String getVmQName()
	{
		return myMsilElement.getVmQName();
	}

	@Nullable
	@Override
	public String getVmName()
	{
		return myMsilElement.getVmName();
	}

	@Override
	public boolean isEquivalentTo(PsiElement another)
	{
		if(another instanceof DotNetTypeDeclaration)
		{
			return Comparing.equal(getPresentableQName(), ((DotNetTypeDeclaration) another).getPresentableQName());
		}
		return super.isEquivalentTo(another);
	}

	@Override
	public boolean hasExtensions()
	{
		return CSharpTypeDeclarationImplUtil.hasExtensions(this);
	}

	@Override
	public PsiElement getLeftBrace()
	{
		return null;
	}

	@Override
	public PsiElement getRightBrace()
	{
		return null;
	}

	@Nullable
	@Override
	public CSharpGenericConstraintList getGenericConstraintList()
	{
		return null;
	}

	@NotNull
	@Override
	public CSharpGenericConstraint[] getGenericConstraints()
	{
		return new CSharpGenericConstraint[0];
	}

	@Override
	public boolean isInterface()
	{
		return myMsilElement.isInterface();
	}

	@Override
	public boolean isStruct()
	{
		return myMsilElement.isStruct();
	}

	@Override
	public boolean isEnum()
	{
		return myMsilElement.isEnum();
	}

	@Override
	public boolean isNested()
	{
		return myMsilElement.isNested();
	}

	@Nullable
	@Override
	public DotNetTypeList getExtendList()
	{
		return null;
	}

	@NotNull
	@Override
	@LazyInstance
	public DotNetTypeRef[] getExtendTypeRefs()
	{
		String vmQName = getVmQName();
		// hack
		if(DotNetTypes.System.Object.equals(vmQName))
		{
			return DotNetTypeRef.EMPTY_ARRAY;
		}
		DotNetTypeRef[] extendTypeRefs = myMsilElement.getExtendTypeRefs();
		if(extendTypeRefs.length == 0)
		{
			return DotNetTypeRef.EMPTY_ARRAY;
		}
		DotNetTypeRef[] typeRefs = new DotNetTypeRef[extendTypeRefs.length];
		for(int i = 0; i < typeRefs.length; i++)
		{
			typeRefs[i] = MsilToCSharpUtil.extractToCSharp(extendTypeRefs[i], myMsilElement);
		}
		return typeRefs;
	}

	@Nullable
	@Override
	public DotNetFieldDeclaration findFieldByName(@NotNull String name, boolean dep)
	{
		return DotNetTypeDeclarationUtil.findFieldByName(this, name, dep);
	}

	@Override
	public boolean isInheritor(@NotNull DotNetTypeDeclaration other, boolean deep)
	{
		return DotNetInheritUtil.isInheritor(this, other, deep);
	}

	@Override
	public DotNetTypeRef getTypeRefForEnumConstants()
	{
		return MsilToCSharpUtil.extractToCSharp(myMsilElement.getTypeRefForEnumConstants(), myMsilElement);
	}

	@Override
	public void processConstructors(@NotNull Processor<DotNetConstructorDeclaration> processor)
	{
		CSharpTypeDeclarationImplUtil.processConstructors(this, processor);
	}

	@Nullable
	@Override
	public DotNetGenericParameterList getGenericParameterList()
	{
		return myMsilElement.getGenericParameterList();
	}

	@NotNull
	@Override
	public DotNetGenericParameter[] getGenericParameters()
	{
		return myMsilElement.getGenericParameters();
	}

	@Override
	public int getGenericParametersCount()
	{
		return myMsilElement.getGenericParametersCount();
	}

	@NotNull
	@Override
	public DotNetNamedElement[] getMembers()
	{
		return myMembersValue.getValue();
	}

	@Override
	public boolean hasModifier(@NotNull DotNetModifier modifier)
	{
		return myModifierList.hasModifier(modifier);
	}

	@Nullable
	@Override
	public DotNetModifierList getModifierList()
	{
		return myModifierList;
	}

	@Nullable
	@Override
	public String getPresentableParentQName()
	{
		return myMsilElement.getPresentableParentQName();
	}

	@Override
	public String getName()
	{
		return MsilHelper.cutGenericMarker(myMsilElement.getName());
	}

	@Nullable
	@Override
	public String getPresentableQName()
	{
		return MsilHelper.cutGenericMarker(myMsilElement.getPresentableQName());
	}

	@Override
	public String toString()
	{
		return myMsilElement.toString();
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return myMsilElement.getNameIdentifier();
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return ItemPresentationProviders.getItemPresentation(this);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException
	{
		return null;
	}
}
