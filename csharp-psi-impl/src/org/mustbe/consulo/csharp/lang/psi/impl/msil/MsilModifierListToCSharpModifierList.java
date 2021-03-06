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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.CSharpLanguage;
import org.mustbe.consulo.csharp.lang.psi.CSharpModifier;
import org.mustbe.consulo.csharp.lang.psi.impl.light.CSharpLightAttributeBuilder;
import org.mustbe.consulo.csharp.lang.psi.impl.light.CSharpLightAttributeWithSelfTypeBuilder;
import org.mustbe.consulo.dotnet.DotNetTypes;
import org.mustbe.consulo.dotnet.externalAttributes.ExternalAttributeArgumentNode;
import org.mustbe.consulo.dotnet.externalAttributes.ExternalAttributeHolder;
import org.mustbe.consulo.dotnet.externalAttributes.ExternalAttributeNode;
import org.mustbe.consulo.dotnet.externalAttributes.ExternalAttributesUtil;
import org.mustbe.consulo.dotnet.psi.DotNetAttribute;
import org.mustbe.consulo.dotnet.psi.DotNetModifier;
import org.mustbe.consulo.dotnet.psi.DotNetModifierList;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import org.mustbe.consulo.msil.lang.psi.MsilModifierList;
import org.mustbe.consulo.msil.lang.psi.MsilTokens;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 23.05.14
 */
public class MsilModifierListToCSharpModifierList extends LightElement implements DotNetModifierList
{
	private static final String[] ourAttributeBans = new String[] {
			DotNetTypes.System.Runtime.CompilerServices.ExtensionAttribute
	};

	private final MsilModifierList myModifierList;

	private final CSharpModifier[] myAdditional;
	private List<DotNetAttribute> myAdditionalAttributes = Collections.emptyList();

	public MsilModifierListToCSharpModifierList(MsilModifierList modifierList)
	{
		this(CSharpModifier.EMPTY_ARRAY, modifierList);
	}

	public MsilModifierListToCSharpModifierList(@NotNull CSharpModifier[] additional, MsilModifierList modifierList)
	{
		super(PsiManager.getInstance(modifierList.getProject()), CSharpLanguage.INSTANCE);
		myAdditional = additional;
		myModifierList = modifierList;

		if(myModifierList.hasModifier(MsilTokens.SERIALIZABLE_KEYWORD))
		{
			addAdditionalAttribute(new CSharpLightAttributeBuilder(myModifierList, DotNetTypes.System.Serializable));
		}
	}

	public void addAdditionalAttribute(@NotNull DotNetAttribute attribute)
	{
		if(myAdditionalAttributes.isEmpty())
		{
			myAdditionalAttributes = new ArrayList<DotNetAttribute>(5);
		}
		myAdditionalAttributes.add(attribute);
	}

	@Override
	public void addModifier(@NotNull DotNetModifier modifier)
	{

	}

	@Override
	public void removeModifier(@NotNull DotNetModifier modifier)
	{

	}

	@NotNull
	@Override
	public DotNetModifier[] getModifiers()
	{
		List<CSharpModifier> list = new ArrayList<CSharpModifier>();
		for(CSharpModifier cSharpModifier : CSharpModifier.values())
		{
			if(MsilToCSharpUtil.hasCSharpInMsilModifierList(cSharpModifier, myModifierList))
			{
				list.add(cSharpModifier);
			}
		}
		Collections.addAll(list, myAdditional);
		return list.toArray(new DotNetModifier[list.size()]);
	}

	@NotNull
	@Override
	public DotNetAttribute[] getAttributes()
	{
		DotNetAttribute[] oldAttributes = myModifierList.getAttributes();
		List<DotNetAttribute> attributes = new ArrayList<DotNetAttribute>(oldAttributes.length + myAdditionalAttributes.size());
		for(DotNetAttribute oldAttribute : oldAttributes)
		{
			DotNetTypeDeclaration resolvedType = oldAttribute.resolveToType();
			if(resolvedType != null && ArrayUtil.contains(resolvedType.getVmQName(), ourAttributeBans))
			{
				continue;
			}
			attributes.add(oldAttribute);
		}
		attributes.addAll(myAdditionalAttributes);

		ExternalAttributeHolder holder = getExternalAttributeHolder();

		if(holder != null)
		{
			List<ExternalAttributeNode> nodes = findAttributes(holder);
			for(ExternalAttributeNode node : nodes)
			{
				CSharpLightAttributeWithSelfTypeBuilder builder = new CSharpLightAttributeWithSelfTypeBuilder(myModifierList, node.getName());

				for(ExternalAttributeArgumentNode argumentNode : node.getArguments())
				{
					builder.addParameterExpression(argumentNode.toJavaObject());
				}
				attributes.add(builder);
			}
		}
		return attributes.toArray(new DotNetAttribute[attributes.size()]);
	}

	@NotNull
	public List<ExternalAttributeNode> findAttributes(ExternalAttributeHolder holder)
	{
		return Collections.emptyList();
	}

	@LazyInstance(notNull = false)
	private ExternalAttributeHolder getExternalAttributeHolder()
	{
		return ExternalAttributesUtil.findHolder(myModifierList);
	}

	@Override
	public boolean hasModifier(@NotNull DotNetModifier modifier)
	{
		if(ArrayUtil.contains(modifier, myAdditional))
		{
			return true;
		}
		CSharpModifier cSharpModifier = CSharpModifier.as(modifier);
		return MsilToCSharpUtil.hasCSharpInMsilModifierList(cSharpModifier, myModifierList);
	}

	@Override
	public boolean hasModifierInTree(@NotNull DotNetModifier modifier)
	{
		return hasModifier(modifier);
	}

	@Nullable
	@Override
	public PsiElement getModifierElement(DotNetModifier modifier)
	{
		return null;
	}

	@NotNull
	@Override
	public List<PsiElement> getModifierElements(@NotNull DotNetModifier modifier)
	{
		return Collections.emptyList();
	}

	@Override
	public String toString()
	{
		return myModifierList.toString();
	}
}
