package org.mustbe.consulo.csharp.ide.highlight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeHighlighting.MainHighlightingPassFactory;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar;
import com.intellij.codeInsight.daemon.ProblemHighlightFilter;
import com.intellij.codeInsight.daemon.impl.DefaultHighlightInfoProcessor;
import com.intellij.codeInsight.daemon.impl.FileStatusMap;
import com.intellij.codeInsight.daemon.impl.HighlightInfoProcessor;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiModificationTracker;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public class CSharpPostHighlightPassFactory extends AbstractProjectComponent implements MainHighlightingPassFactory
{
	private static final Key<Long> LAST_POST_PASS_TIMESTAMP = Key.create("CSHARP_LAST_POST_PASS_TIMESTAMP");

	public CSharpPostHighlightPassFactory(Project project, TextEditorHighlightingPassRegistrar highlightingPassRegistrar)
	{
		super(project);
		highlightingPassRegistrar.registerTextEditorHighlightingPass(this, new int[]{Pass.UPDATE_ALL,}, null, true, Pass.POST_UPDATE_ALL);
	}

	@Override
	@Nullable
	public TextEditorHighlightingPass createHighlightingPass(@NotNull PsiFile file, @NotNull final Editor editor)
	{
		TextRange textRange = FileStatusMap.getDirtyTextRange(editor, Pass.UPDATE_ALL);
		if(textRange == null)
		{
			Long lastStamp = file.getUserData(LAST_POST_PASS_TIMESTAMP);
			long currentStamp = PsiModificationTracker.SERVICE.getInstance(myProject).getModificationCount();
			if(lastStamp != null && lastStamp == currentStamp || !ProblemHighlightFilter.shouldHighlightFile(file))
			{
				return null;
			}
		}

		return new CSharpPostHighlightPass(myProject, file, editor, editor.getDocument(), new DefaultHighlightInfoProcessor());
	}

	@Override
	public TextEditorHighlightingPass createMainHighlightingPass(@NotNull PsiFile file, @NotNull Document document,
			@NotNull HighlightInfoProcessor highlightInfoProcessor)
	{
		return new CSharpPostHighlightPass(myProject, file, null, document, highlightInfoProcessor);
	}

	public static void markFileUpToDate(@NotNull PsiFile file)
	{
		long lastStamp = PsiModificationTracker.SERVICE.getInstance(file.getProject()).getModificationCount();
		file.putUserData(LAST_POST_PASS_TIMESTAMP, lastStamp);
	}

}
