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
import org.mustbe.consulo.csharp.lang.psi.CSharpBinaryExpression;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.dotnet.psi.DotNetExpression;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 29.12.13.
 */
public class CSharpBinaryExpressionImpl extends CSharpExpressionWithOperatorImpl implements CSharpBinaryExpression
{
	public CSharpBinaryExpressionImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Nullable
	public DotNetExpression getLeftExpression()
	{
		return findChildByClass(DotNetExpression.class);
	}

	@Nullable
	public DotNetExpression getRightExpression()
	{
		PsiElement operatorElement = getOperatorElement();
		PsiElement nextSibling = operatorElement.getNextSibling();
		while(nextSibling != null)
		{
			if(nextSibling instanceof DotNetExpression)
			{
				return (DotNetExpression) nextSibling;
			}
			nextSibling = nextSibling.getNextSibling();
		}
		return null;
	}

	@Override
	public <P, R> R accept(CSharpElementVisitor<P, R> visitor, P p)
	{
		return visitor.visitBinaryExpression(this, p);
	}
}
