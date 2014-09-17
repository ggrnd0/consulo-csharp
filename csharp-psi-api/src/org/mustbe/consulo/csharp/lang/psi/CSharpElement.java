package org.mustbe.consulo.csharp.lang.psi;

import org.consulo.lombok.annotations.ArrayFactoryFields;
import org.mustbe.consulo.dotnet.psi.DotNetElement;

/**
 * @author VISTALL
 * @since 16.09.14
 */
@ArrayFactoryFields
public interface CSharpElement extends DotNetElement
{
	<P, R> R accept(CSharpElementVisitor<P, R> visitor, P p);
}
