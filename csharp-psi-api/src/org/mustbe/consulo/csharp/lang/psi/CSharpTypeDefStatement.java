package org.mustbe.consulo.csharp.lang.psi;

import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public interface CSharpTypeDefStatement extends CSharpElement, DotNetNamedElement, PsiNameIdentifierOwner, CSharpUsingListChild
{
}
