package org.mustbe.consulo.csharp.lang.psi;

import org.mustbe.consulo.dotnet.psi.DotNetArrayType;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public interface CSharpArrayType extends DotNetArrayType
{
	int getDimensions();
}
