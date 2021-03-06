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

package org.mustbe.consulo.csharp.module.extension;

import org.consulo.module.extension.MutableModuleExtension;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 15.05.14
 */
public interface CSharpMutableModuleExtension<T extends CSharpModuleExtension<T>> extends CSharpModuleExtension<T>, MutableModuleExtension<T>
{
	MutableModuleInheritableNamedPointer<CSharpLanguageVersion> getLanguageVersionPointer();

	void setLanguageVersion(@NotNull CSharpLanguageVersion version);

	void setAllowUnsafeCode(boolean value);

	void setOptimizeCode(boolean value);

	void setPlatform(@NotNull CSharpPlatform platform);
}
