/*
Redline Smalltalk is licensed under the MIT License

Redline Smalltalk Copyright (c) 2010 James C. Ladd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Please see DEVELOPER-CERTIFICATE-OF-ORIGIN if you wish to contribute a patch to Redline Smalltalk.
*/
package st.redline;

import java.util.HashMap;
import java.util.Map;

public class ProtoObject {

	private static final Map<String, ProtoObject> classRegistry = new HashMap<String, ProtoObject>();

	private Data data;

	public ProtoObject() {
		this(true);
	}

	public ProtoObject(boolean isClass) {
		data = isClass ? new ClassData() : new InstanceData();
	}

	public static void primitiveMain(ProtoObject receiver, String[] args) {
	}

	public static ProtoObject primitiveRegisterAs(ProtoObject receiver, String name) {
		classRegistry.put(name, receiver);
		return receiver;
	}

	public static ProtoObject primitiveVariableAt(ProtoObject receiver, String name) {
		if (primitiveIsInstanceVariable(receiver, name))
			throw new IllegalStateException("todo - implement");
		if (primitiveIsClassVariable(receiver, name))
			throw new IllegalStateException("todo - implement");
		return primitiveResolveObject(receiver, name);
	}

	public static boolean primitiveIsInstanceVariable(ProtoObject receiver, String name) {
		return false;
	}

	public static boolean primitiveIsClassVariable(ProtoObject receiver, String name) {
		return false;
	}

	public static ProtoObject primitiveSymbol(ProtoObject receiver, String value) {
		ProtoObject symbolClass = receiver.resolveObject("Symbol");
		ProtoObject symbol = new ProtoObject(false);
		symbol.cls(symbolClass);
		symbol.javaValue(value);
		return symbol;
	}

	public static ProtoObject primitiveSend(ProtoObject receiver, String selector, ProtoObject classMethodWasFoundIn) {
		throw new IllegalStateException("todo - implement " + selector);
	}

	public static ProtoObject primitiveSend(ProtoObject receiver, ProtoObject arg1, String selector, ProtoObject classMethodWasFoundIn) {
		throw new IllegalStateException("todo - implement " + selector + " " + arg1);
	}

	public static ProtoObject primitiveResolveObject(ProtoObject receiver, String name) {
		ProtoObject object = receiver.resolveObject(name);
		if (object != null)
			return object;
		// TODO.JCL search through namespaces to find object.
		return receiver.resolveObject("st.redline." + name);
	}

	private ProtoObject resolveObject(String name) {
		System.out.println("resolveObject() " + name);
		if (classRegistry.containsKey(name)) {
			return classRegistry.get(name);
		}
		// It is expected the loading of an object results in the registering a Smalltalk class in the class registry.
		// *NOTE* if class is not registered the will be a NullPointerException as we return 'null' here.
		if (loadObject(name))
			return classRegistry.get(name);
		// TODO.JCL should we return 'nil'?
		throw new IllegalStateException("Handle this - Class not found.");
	}

	private boolean loadObject(String name) {
		try {
			return Class.forName(name, true, classLoader()).newInstance() != null;
		} catch (Exception e) {
			return false;
		}
	}

	private ClassLoader classLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void bootstrap() {
		new Bootstrapper(this).bootstrap();
	}

	public void bootstrapped() {
		data.bootstrapped();
	}

	public boolean isBootstrapped() {
		return data.isBootstrapped();
	}

	protected void cls(ProtoObject cls) {
		data.cls(cls);
	}

	protected ProtoObject cls() {
		return data.cls();
	}

	protected void javaValue(String value) {
		data.javaValue(value);
	}

	protected String javaValue() {
		return data.javaValue();
	}

	abstract class Data {

		private ProtoObject cls;
		private Map<String, ProtoObject> variables;
		private boolean bootstrapped = false;

		abstract void javaValue(String value);
		abstract String javaValue();

		protected void cls(ProtoObject cls) {
			this.cls = cls;
		}

		protected ProtoObject cls() {
			return cls;
		}

		protected void bootstrapped() {
			bootstrapped = true;
		}

		protected boolean isBootstrapped() {
			return bootstrapped;
		}
	}

	class InstanceData extends Data {

		private String javaValue;

		protected void javaValue(String value) {
			javaValue = value;
		}

		protected String javaValue() {
			return javaValue;
		}
	}

	class ClassData extends Data {

		private ProtoObject superclass;
		private Map<String, ProtoObject> methods;

		protected void javaValue(String value) {
			throw new IllegalStateException("A Class can't have a javaValue.");
		}

		protected String javaValue() {
			throw new IllegalStateException("A Class doesn't have a javaValue.");
		}
	}
}
