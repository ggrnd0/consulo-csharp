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

package org.mustbe.consulo.csharp.lang.psi.impl.source;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joou.Unsigned;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.csharp.lang.psi.CSharpTokens;
import org.mustbe.consulo.csharp.lang.psi.impl.msil.CSharpTransform;
import org.mustbe.consulo.csharp.lang.psi.impl.source.injection.CSharpStringLiteralEscaper;
import org.mustbe.consulo.csharp.lang.psi.impl.source.resolve.type.CSharpConstantTypeRef;
import org.mustbe.consulo.dotnet.DotNetTypes;
import org.mustbe.consulo.dotnet.lang.psi.impl.source.resolve.type.DotNetTypeRefByQName;
import org.mustbe.consulo.dotnet.psi.DotNetConstantExpression;
import org.mustbe.consulo.dotnet.resolve.DotNetTypeRef;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 16.12.13.
 */
public class CSharpConstantExpressionImpl extends CSharpElementImpl implements DotNetConstantExpression, PsiLanguageInjectionHost
{
	public CSharpConstantExpressionImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull CSharpElementVisitor visitor)
	{
		visitor.visitConstantExpression(this);
	}

	@NotNull
	@Override
	public DotNetTypeRef toTypeRef(boolean resolveFromParent)
	{
		PsiElement byType = getFirstChild();
		assert byType != null;
		IElementType elementType = byType.getNode().getElementType();
		if(elementType == CSharpTokens.STRING_LITERAL || elementType == CSharpTokens.VERBATIM_STRING_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.String, CSharpTransform.INSTANCE);
		}
		else if(elementType == CSharpTokens.CHARACTER_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.Char, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.UINTEGER_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.UInt32, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.ULONG_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.UInt64, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.INTEGER_LITERAL)
		{
			return new CSharpConstantTypeRef(new DotNetTypeRefByQName(DotNetTypes.System.Int32, CSharpTransform.INSTANCE, false));
		}
		else if(elementType == CSharpTokens.LONG_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.Int64, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.FLOAT_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.Single, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.DOUBLE_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.Double, CSharpTransform.INSTANCE, false);
		}
		else if(elementType == CSharpTokens.NULL_LITERAL)
		{
			return DotNetTypeRef.NULL_TYPE;
		}
		else if(elementType == CSharpTokens.BOOL_LITERAL)
		{
			return new DotNetTypeRefByQName(DotNetTypes.System.Boolean, CSharpTransform.INSTANCE, false);
		}
		return DotNetTypeRef.ERROR_TYPE;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		PsiElement byType = getFirstChild();
		assert byType != null;
		IElementType elementType = byType.getNode().getElementType();
		String text = getText();
		if(elementType == CSharpTokens.STRING_LITERAL)
		{
			return StringUtil.unquoteString(text);
		}
		else if(elementType == CSharpTokens.VERBATIM_STRING_LITERAL)
		{
			return getText(); //TODO [VISTALL] unquote @ "" and escape \n \t
		}
		else if(elementType == CSharpTokens.CHARACTER_LITERAL)
		{
			return StringUtil.unquoteString(text).charAt(0);
		}
		else if(elementType == CSharpTokens.UINTEGER_LITERAL)
		{
			text = text.substring(0, text.length() - 1); //cut U
			return Unsigned.uint(text);
		}
		else if(elementType == CSharpTokens.ULONG_LITERAL)
		{
			text = text.substring(0, text.length() - 2); //cut UL
			return Unsigned.ulong(text);
		}
		else if(elementType == CSharpTokens.INTEGER_LITERAL)
		{
			return Integer.parseInt(text);
		}
		else if(elementType == CSharpTokens.LONG_LITERAL)
		{
			return Long.parseLong(text);
		}
		else if(elementType == CSharpTokens.FLOAT_LITERAL)
		{
			return Float.parseFloat(text);
		}
		else if(elementType == CSharpTokens.DOUBLE_LITERAL)
		{
			return Double.parseDouble(text);
		}
		else if(elementType == CSharpTokens.NULL_LITERAL)
		{
			return null;
		}
		else if(elementType == CSharpTokens.BOOL_LITERAL)
		{
			return Boolean.parseBoolean(text);
		}
		throw new IllegalArgumentException(elementType.toString());
	}

	@NotNull
	@Override
	public IElementType getLiteralType()
	{
		PsiElement byType = getFirstChild();
		assert byType != null;
		return byType.getNode().getElementType();
	}

	@Override
	public boolean isValidHost()
	{
		IElementType elementType = getLiteralType();
		return elementType == CSharpTokens.STRING_LITERAL || elementType == CSharpTokens.VERBATIM_STRING_LITERAL;
	}

	@Override
	public PsiLanguageInjectionHost updateText(@NotNull String s)
	{
		LeafPsiElement first = (LeafPsiElement) getFirstChild();
		first.replaceWithText(s);
		return this;
	}

	@NotNull
	@Override
	public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper()
	{
		PsiElement byType = getFirstChild();
		assert byType != null;
		IElementType elementType = byType.getNode().getElementType();
		if(elementType == CSharpTokens.STRING_LITERAL)
		{
			return new CSharpStringLiteralEscaper<CSharpConstantExpressionImpl>(this);
		}
		else if(elementType == CSharpTokens.VERBATIM_STRING_LITERAL)
		{
			return LiteralTextEscaper.createSimple(this);
		}
		throw new IllegalArgumentException("Unknown " + elementType);
	}
}
