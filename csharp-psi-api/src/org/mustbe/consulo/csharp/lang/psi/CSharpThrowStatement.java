package org.mustbe.consulo.csharp.lang.psi;

import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.psi.DotNetExpression;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public interface CSharpThrowStatement extends CSharpStatement
{
	@Nullable
	DotNetExpression getExpression();
}
