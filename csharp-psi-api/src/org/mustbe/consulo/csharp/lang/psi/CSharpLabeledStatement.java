package org.mustbe.consulo.csharp.lang.psi;

import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import org.mustbe.consulo.dotnet.psi.DotNetStatement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public interface CSharpLabeledStatement extends DotNetStatement, PsiNameIdentifierOwner, DotNetNamedElement, CSharpElement
{
}
