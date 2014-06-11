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

package org.mustbe.consulo.csharp.lang.psi.impl.source.resolve.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.resolve.DotNetRefTypeRef;
import org.mustbe.consulo.dotnet.resolve.DotNetTypeRef;

/**
 * @author VISTALL
 * @since 02.06.14
 */
public class CSharpRefTypeRef extends DotNetTypeRef.Delegate implements DotNetRefTypeRef
{
	public static enum Type
	{
		out,
		ref
	}

	private final Type myType;

	public CSharpRefTypeRef(Type type, DotNetTypeRef typeRef)
	{
		super(typeRef);
		myType = type;
	}

	public Type getType()
	{
		return myType;
	}

	@NotNull
	@Override
	public String getPresentableText()
	{
		return myType.name() + " " + super.getPresentableText();
	}

	@NotNull
	@Override
	public String getQualifiedText()
	{
		return myType.name() + " " + super.getQualifiedText();
	}

	@NotNull
	@Override
	public DotNetTypeRef getInnerTypeRef()
	{
		return getDelegate();
	}
}
