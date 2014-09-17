package org.mustbe.consulo.csharp.ide.highlight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.CSharpRecursiveElementVisitor;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFileImpl;
import com.intellij.codeInsight.daemon.impl.HighlightInfoProcessor;
import com.intellij.codeInsight.daemon.impl.ProgressableTextEditorHighlightingPass;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public class CSharpPostHighlightPass extends ProgressableTextEditorHighlightingPass
{
	public CSharpPostHighlightPass(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor, @Nullable Document document,
			@NotNull HighlightInfoProcessor highlightInfoProcessor)
	{
		super(project, document, "Unused symbols", file, editor, file.getTextRange(), true, highlightInfoProcessor);
	}

	@Override
	protected void collectInformationWithProgress(@NotNull ProgressIndicator progress)
	{
		if(!(myFile instanceof CSharpFileImpl))
		{
			return;
		}

		myFile.accept(new CSharpRecursiveElementVisitor()
		{

		});
	}

	@Override
	protected void applyInformationWithProgress()
	{

	}
}
