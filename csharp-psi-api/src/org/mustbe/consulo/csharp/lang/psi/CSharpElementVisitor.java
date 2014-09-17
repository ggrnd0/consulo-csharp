package org.mustbe.consulo.csharp.lang.psi;

import org.mustbe.consulo.dotnet.psi.DotNetFieldDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetGenericParameter;
import org.mustbe.consulo.dotnet.psi.DotNetGenericParameterList;
import org.mustbe.consulo.dotnet.psi.DotNetModifierList;
import org.mustbe.consulo.dotnet.psi.DotNetParameter;
import org.mustbe.consulo.dotnet.psi.DotNetParameterList;
import org.mustbe.consulo.dotnet.psi.DotNetTypeList;
import org.mustbe.consulo.dotnet.psi.DotNetUserType;
import org.mustbe.consulo.dotnet.psi.DotNetXXXAccessor;

/**
 * @author VISTALL
 * @since 16.09.14
 */
public interface CSharpElementVisitor<P, R>
{
	R visitCSharpFile(CSharpFile file, P p);

	R visitUsingNamespaceList(CSharpUsingNamespaceList list, P p);

	R visitConstructorDeclaration(CSharpConstructorDeclaration declaration, P p);

	R visitMethodDeclaration(CSharpMethodDeclaration declaration, P p);

	R visitModifierList(DotNetModifierList list, P p);

	R visitNamespaceDeclaration(CSharpNamespaceDeclaration declaration, P p);

	R visitParameter(DotNetParameter parameter, P p);

	R visitParameterList(DotNetParameterList list, P p);

	R visitReferenceExpression(CSharpReferenceExpression expression, P p);

	R visitTypeDeclaration(CSharpTypeDeclaration declaration, P p);

	R visitReferenceType(DotNetUserType type, P p);

	R visitUsingNamespaceStatement(CSharpUsingNamespaceStatement statement, P p);

	R visitGenericParameter(DotNetGenericParameter parameter, P p);

	R visitGenericParameterList(DotNetGenericParameterList list, P p);

	R visitGenericConstraintList(CSharpGenericConstraintList list, P p);

	R visitGenericConstraint(CSharpGenericConstraint constraint, P p);

	R visitGenericConstraintKeywordValue(CSharpGenericConstraintKeywordValue value, P p);

	R visitGenericConstraintTypeValue(CSharpGenericConstraintTypeValue value, P p);

	R visitTypeList(DotNetTypeList list, P p);

	R visitEventDeclaration(CSharpEventDeclaration declaration, P p);

	R visitPropertyDeclaration(CSharpPropertyDeclaration declaration, P p);

	R visitXXXAccessor(DotNetXXXAccessor accessor, P p);

	R visitFieldDeclaration(DotNetFieldDeclaration declaration, P p);

	R visitPointerType(CSharpPointerType type, P p);

	R visitNullableType(CSharpNullableType type, P p);

	R visitNativeType(CSharpNativeType type, P p);

	R visitTypeWrapperWithTypeArguments(CSharpTypeWithTypeArguments typeArguments, P p);

	R visitArrayType(CSharpArrayType type, P p);

	R visitLocalVariable(CSharpLocalVariable variable, P p);

	R visitConstantExpression(CSharpConstantExpression expression, P p);

	R visitLocalVariableDeclarationStatement(CSharpLocalVariableDeclarationStatement statement, P p);

	R visitExpressionStatement(CSharpExpressionStatement statement, P p);

	R visitMethodCallExpression(CSharpMethodCallExpression expression, P p);

	R visitMethodCallParameterList(CSharpCallArgumentList list, P p);

	R visitTypeOfExpression(CSharpTypeOfExpression expression, P p);

	R visitAttributeList(CSharpAttributeList list, P p);

	R visitAttribute(CSharpAttribute attribute, P p);

	R visitBinaryExpression(CSharpBinaryExpression expression, P p);

	R visitNewExpression(CSharpNewExpression expression, P p);

	R visitFieldOrPropertySetBlock(CSharpFieldOrPropertySetBlock block, P p);

	R visitFieldOrPropertySet(CSharpFieldOrPropertySet element, P p);

	R visitLockStatement(CSharpLockStatement statement, P p);

	R visitParenthesesExpression(CSharpParenthesesExpression expression, P p);

	R visitBreakStatement(CSharpBreakStatement statement, P p);

	R visitContinueStatement(CSharpContinueStatement statement, P p);

	R visitReturnStatement(CSharpReturnStatement statement, P p);

	R visitYieldStatement(CSharpYieldStatement statement, P p);

	R visitWhileStatement(CSharpWhileStatement statement, P p);

	R visitIsExpression(CSharpIsExpression expression, P p);

	R visitConditionalExpression(CSharpConditionalExpression expression, P p);

	R visitNullCoalescingExpression(CSharpNullCoalescingExpression expression, P p);

	R visitAssignmentExpression(CSharpAssignmentExpression expression, P p);

	R visitTypeCastExpression(CSharpTypeCastExpression expression, P p);

	R visitArrayAccessExpression(CSharpArrayAccessExpression expression, P p);

	R visitPostfixExpression(CSharpPostfixExpression expression, P p);

	R visitPrefixExpression(CSharpPrefixExpression expression, P p);

	R visitLambdaExpression(CSharpLambdaExpression expression, P p);

	R visitLinqExpression(CSharpLinqExpression expression, P p);

	R visitLinqFrom(CSharpLinqFrom select, P p);

	R visitLinqIn(CSharpLinqIn in, P p);

	R visitLinqLet(CSharpLinqLet let, P p);

	R visitLinqWhere(CSharpLinqWhere where, P p);

	R visitLinqSelect(CSharpLinqSelect select, P p);

	R visitForeachStatement(CSharpForeachStatement statement, P p);

	R visitIfStatement(CSharpIfStatement statement, P p);

	R visitBlockStatement(CSharpBlockStatement statement, P p);

	R visitAsExpression(CSharpAsExpression expression, P p);

	R visitDefaultExpression(CSharpDefaultExpression expression, P p);

	R visitUsingStatement(CSharpUsingStatement statement, P p);

	R visitSizeOfExpression(CSharpSizeOfExpression expression, P p);

	R visitFixedStatement(CSharpFixedStatement statement, P p);

	R visitGotoStatement(CSharpGotoStatement element, P p);

	R visitLabeledStatement(CSharpLabeledStatement statement, P p);

	R visitEnumConstantDeclaration(CSharpEnumConstantDeclaration declaration, P p);

	R visitConversionMethodDeclaration(CSharpConversionMethodDeclaration element, P p);

	R visitDoWhileStatement(CSharpDoWhileStatement statement, P p);

	R visitEmptyStatement(CSharpEmptyStatement statement, P p);

	R visitForStatement(CSharpForStatement statement, P p);

	R visitTryStatement(CSharpTryStatement statement, P p);

	R visitCatchStatement(CSharpCatchStatement statement, P p);

	R visitFinallyStatement(CSharpFinallyStatement statement, P p);

	R visitThrowStatement(CSharpThrowStatement statement, P p);

	R visitLambdaParameter(CSharpLambdaParameter parameter, P p);

	R visitLambdaParameterList(CSharpLambdaParameterList list, P p);

	R visitAnonymMethod(CSharpAnonymMethodExpression method, P p);

	R visitArrayInitializationExpression(CSharpArrayInitializationExpression expression, P p);

	R visitTypeDefStatement(CSharpTypeDefStatement statement, P p);

	R visitCheckedStatement(CSharpCheckedStatement statement, P p);

	R visitCheckedExpression(CSharpCheckedExpression expression, P p);

	R visitOurRefWrapExpression(CSharpOutRefWrapExpression expression, P p);

	R visitSwitchStatement(CSharpSwitchStatement statement, P p);

	R visitSwitchLabelStatement(CSharpSwitchLabelStatement statement, P p);

	R visitArrayMethodDeclaration(CSharpArrayMethodDeclaration methodDeclaration, P p);

	R visitDummyDeclaration(CSharpDummyDeclaration declaration, P p);

	R visitOperatorReference(CSharpOperatorReference referenceExpression, P p);

	R visitConstructorSuperCall(CSharpConstructorSuperCall call, P p);

	R visitNewArrayLength(CSharpNewArrayLength element, P p);

	R visitCallArgument(CSharpCallArgument argument, P p);

	R visitNamedCallArgument(CSharpNamedCallArgument argument, P p);

	R visitAwaitExpression(CSharpAwaitExpression expression, P p);
}
