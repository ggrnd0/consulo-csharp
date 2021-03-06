/*
 * Copyright 2013-2014 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mustbe.consulo.csharp.lang.psi.impl.stub;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.CSharpArrayMethodDeclaration;
import org.mustbe.consulo.csharp.lang.psi.CSharpStubElements;
import org.mustbe.consulo.csharp.lang.psi.impl.stub.typeStub.CSharpStubTypeInfo;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 01.03.14
 */
public class CSharpArrayMethodStub extends MemberStub<CSharpArrayMethodDeclaration>
{
	private final CSharpStubTypeInfo myReturnType;
	private final CSharpStubTypeInfo myImplementType;

	public CSharpArrayMethodStub(StubElement parent, @Nullable StringRef name, @Nullable StringRef qname, int modifierMask,
			CSharpStubTypeInfo returnType, CSharpStubTypeInfo implementType)
	{
		super(parent, CSharpStubElements.ARRAY_METHOD_DECLARATION, name, qname, modifierMask, 0);
		myReturnType = returnType;
		myImplementType = implementType;
	}

	@NotNull
	public CSharpStubTypeInfo getReturnType()
	{
		return myReturnType;
	}

	@NotNull
	public CSharpStubTypeInfo getImplementType()
	{
		return myImplementType;
	}
}
