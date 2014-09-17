package org.mustbe.consulo.csharp.lang.psi.impl.source;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.psi.CSharpCheckedStatement;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.csharp.lang.psi.CSharpTokens;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @since 11.02.14
 */
public class CSharpCheckedStatementImpl extends CSharpElementImpl implements CSharpCheckedStatement
{
	public CSharpCheckedStatementImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public boolean isUnchecked()
	{
		return findChildByType(CSharpTokens.UNCHECKED_KEYWORD) != null;
	}

	@Override
	public <P, R> R accept(CSharpElementVisitor<P, R> visitor, P p)
	{
		return visitor.visitCheckedStatement(this, p);
	}
}
