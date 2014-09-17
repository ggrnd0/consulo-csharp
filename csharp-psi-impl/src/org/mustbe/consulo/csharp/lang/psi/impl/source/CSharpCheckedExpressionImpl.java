package org.mustbe.consulo.csharp.lang.psi.impl.source;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.CSharpCheckedExpression;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.csharp.lang.psi.CSharpTokens;
import org.mustbe.consulo.dotnet.psi.DotNetExpression;
import org.mustbe.consulo.dotnet.resolve.DotNetTypeRef;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @since 11.02.14
 */
public class CSharpCheckedExpressionImpl extends CSharpElementImpl implements CSharpCheckedExpression
{
	public CSharpCheckedExpressionImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public boolean isUnchecked()
	{
		return findChildByType(CSharpTokens.UNCHECKED_KEYWORD) != null;
	}

	@NotNull
	@Override
	public DotNetTypeRef toTypeRef(boolean resolveFromParent)
	{
		DotNetExpression innerExpression = getInnerExpression();
		return innerExpression == null ? DotNetTypeRef.ERROR_TYPE : innerExpression.toTypeRef(true);
	}

	@Nullable
	public DotNetExpression getInnerExpression()
	{
		return findChildByClass(DotNetExpression.class);
	}

	@Override
	public <P, R> R accept(CSharpElementVisitor<P, R> visitor, P p)
	{
		return visitor.visitCheckedExpression(this, p);
	}
}
