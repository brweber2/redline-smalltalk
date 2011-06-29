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
package st.redline.interpreter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.util.Stack;

public class Generator implements Opcodes {

	private static final String SUPERCLASS_FULLY_QUALIFIED_NAME = "st/redline/RMethod";
	private static final String BLOCK_SUPERCLASS_FULLY_QUALIFIED_NAME = "st/redline/RBlock";
	private static final String SMALLTALK_CLASS = "st/redline/Smalltalk";
	private static final String SEND_METHOD_NAME = "send";
	private static final String SUPER_SEND_METHOD_NAME = "superSend";
	private static final int MAXIMUM_KEYWORD_ARGUMENTS = 10;

	private static final String[] SEND_METHOD_DESCRIPTORS = {
		"(Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Ljava/lang/String;Lst/redline/RObject;)Lst/redline/RObject;",
	};

	private static final String[] APPLY_METHOD_DESCRIPTORS = {
		"(Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;",
		"(Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;Lst/redline/RObject;)Lst/redline/RObject;"
	};

	private final int startingLineNumber;

	private Context current = new Context();
	private Stack<Context> contexts = new Stack<Context>();
	private boolean verboseOn;

	public Generator(int startingLineNumber, boolean verboseOn) {
		this.startingLineNumber = startingLineNumber;
		this.verboseOn = verboseOn;
		initialize();
	}

	public byte[] build() {
		return current.classWriter.toByteArray();
	}

	public void openClass(String className, String packageName, String sourceFileExtension) {
		openClass(className, packageName, sourceFileExtension, SUPERCLASS_FULLY_QUALIFIED_NAME);
	}

	public void openClass(String className, String packageName, String sourceFileExtension, String superclass) {
		remember(className, packageName, sourceFileExtension);
		createClass(className, sourceFileExtension, superclass);
		openInitializeMethod();
	}

	public void openBlockClass(String className, String packageName, String sourceFileExtension) {
		openContext();
		remember(className, packageName, sourceFileExtension);
		createClass(className, sourceFileExtension, BLOCK_SUPERCLASS_FULLY_QUALIFIED_NAME);
		openInitializeMethod();
	}

	public void createBlock(String blockName, String packageName, String sourceFileExtension, int methodArgumentCount) {
		// System.out.println("createBlock() " + blockName);
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(blockName);
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "createBlock", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void openMethod(int countOfArguments) {
		if (countOfArguments > MAXIMUM_KEYWORD_ARGUMENTS)
			throw new IllegalArgumentException("More than " + MAXIMUM_KEYWORD_ARGUMENTS + " applyTo method arguments!");
		cloneContext();
		String selector = countOfArguments == 0 ? "applyTo" : "applyToWith";
		current.methodVisitor = current.classWriter.visitMethod(ACC_PUBLIC, selector, APPLY_METHOD_DESCRIPTORS[countOfArguments], null, null);
		current.methodVisitor.visitCode();
	}

	public void openBlock(int countOfArguments, boolean hasSequenceOfStatements) {
		if (countOfArguments > MAXIMUM_KEYWORD_ARGUMENTS)
			throw new IllegalArgumentException("More than " + MAXIMUM_KEYWORD_ARGUMENTS + " block arguments!");
		cloneContext();
		String selector = countOfArguments == 0 ? "applyTo" : "applyToWith";
		current.methodVisitor = current.classWriter.visitMethod(ACC_PUBLIC, selector, APPLY_METHOD_DESCRIPTORS[countOfArguments], null, null);
		if (!hasSequenceOfStatements)
			current.methodVisitor.visitVarInsn(ALOAD, 0); // push receiver.
		current.methodVisitor.visitCode();
	}

	private void createClass(String className, String sourceFileExtension, String superclass) {
		current.classWriter.visit(V1_5, ACC_PUBLIC + ACC_SUPER, current.fullyQualifiedName, null, superclass, null);
		current.classWriter.visitSource(homogenize(className) + "." + sourceFileExtension, null);
	}

	private String homogenize(String className) {
		int index = className.indexOf("$");
		if (index == -1)
			return className;
		return className.substring(0, index);
	}

	private void remember(String className, String packageName, String sourceFileExtension) {
		current.className = className;
		current.packageName = packageName;
		current.sourceFileExtension = sourceFileExtension;
		current.fullyQualifiedName = packageName + File.separator + className;
	}

	private void openInitializeMethod() {
		current.methodVisitor = current.classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		current.methodVisitor.visitCode();
		invokeSuperclassInitMethod();
	}

	private void invokeSuperclassInitMethod() {
		current.methodVisitor.visitVarInsn(ALOAD, 0);
		current.methodVisitor.visitMethodInsn(INVOKESPECIAL, SUPERCLASS_FULLY_QUALIFIED_NAME, "<init>", "()V");
	}

	public void closeMethod() {
		closeCurrentMethod(true);
		closeContext();
	}

	public void closeBlock() {
		closeCurrentMethod(true);
		closeContext();
	}

	public byte[] closeBlockClass() {
		closeCurrentMethod(false);
		current.classWriter.visitEnd();
		byte[] bytes = build();
		closeContext();
		return bytes;
	}

	public void closeClass() {
		closeCurrentMethod(false);
		current.classWriter.visitEnd();
		closeContext();
	}

	public void closeMethodClass() {
		closeClass();
	}

	private void closeCurrentMethod(boolean answerValue) {
		current.methodVisitor.visitInsn(answerValue ? ARETURN : RETURN);
		current.methodVisitor.visitMaxs(1, 1);
		current.methodVisitor.visitEnd();
	}

	public void classLookup(String className, int line) {
		visitLine(line);
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(className);
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "primitiveAt", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	private void currentSmalltalkClass() {
		current.methodVisitor.visitVarInsn(ALOAD, 0);
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, current.fullyQualifiedName, "smalltalk", "()Lst/redline/Smalltalk;");
	}

	private void visitLine(int line) {
		Label label = new Label();
		current.methodVisitor.visitLabel(label);
		current.methodVisitor.visitLineNumber(line + startingLineNumber, label);
	}

	public void loadFromInstanceField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void storeIntoInstanceField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void loadFromClassInstanceField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void storeIntoClassInstanceField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void loadFromClassField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void storeIntoClassField(String fieldName) {
		throw new IllegalStateException("TODO.");
	}

	public void loadFromLocal(int fieldIndex) {
		current.methodVisitor.visitVarInsn(ALOAD, fieldIndex);
	}

	public void storeIntoLocal(int fieldIndex) {
		current.methodVisitor.visitVarInsn(ASTORE, fieldIndex);
	}

	public void stackPop() {
		current.methodVisitor.visitInsn(POP);
	}

	public void pushStackTop() {
		current.methodVisitor.visitInsn(DUP);
	}

	public void pushReceiver() {
		current.methodVisitor.visitVarInsn(ALOAD, 1);
	}

	public void pushThis() {
		current.methodVisitor.visitVarInsn(ALOAD, 0);
	}

	public void pushForSuperCall(int line) {
		visitLine(line);
		pushReceiver();
	}

	public void trueLookup(int line) {
		invokeSmalltalkObjectMethod(line, "trueInstance");
	}

	public void falseLookup(int line) {
		invokeSmalltalkObjectMethod(line, "falseInstance");
	}

	public void nilLookup(int line) {
		invokeSmalltalkObjectMethod(line, "nilInstance");
	}

	private void invokeSmalltalkObjectMethod(int line, String methodName) {
		visitLine(line);
		currentSmalltalkClass();
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, methodName, "()Lst/redline/RObject;");
	}

	public void primitiveCharacterConversion(String string, int line) {
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(string.substring(1));  // remove leading $
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "characterFromPrimitive", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void primitiveStringChunkConversion(String string, int line) {
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(string.substring(1, string.length() - 1));  // remove ''
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "stringFromPrimitive", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void primitiveStringConversion(String string, int line) {
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(string.substring(1, string.length() - 1));  // remove ''
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "stringFromPrimitive", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void primitiveNumberConversion(String string, int line) {
		currentSmalltalkClass();
		current.methodVisitor.visitLdcInsn(string);
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "numberFromPrimitive", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void primitiveSymbolConversion(String symbol, int line) {
		currentSmalltalkClass();
		if (symbol.startsWith("'"))
			current.methodVisitor.visitLdcInsn(symbol.substring(1, symbol.length() - 1));  // remove ''
		else
			current.methodVisitor.visitLdcInsn(symbol);
		current.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, SMALLTALK_CLASS, "symbolFromPrimitive", "(Ljava/lang/String;)Lst/redline/RObject;");
	}

	public void unarySend(String selector, int line, boolean sendIsToSuper) {
		// System.out.println("unarySend() " + selector);
		visitLine(line);
		current.methodVisitor.visitLdcInsn(selector);
		if (sendIsToSuper) {
			current.methodVisitor.visitVarInsn(ALOAD, 2);  // 0 = this, 1 = receiver, 2 = class method found in.
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SUPER_SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[0]);
		} else {
			current.methodVisitor.visitInsn(ACONST_NULL);
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[0]);
		}
	}

	public void binarySend(String binarySelector, int line, boolean isSendToSuper) {
		// System.out.println("binarySend() " + binarySelector);
		visitLine(line);
		current.methodVisitor.visitLdcInsn(binarySelector);
		if (isSendToSuper) {
			current.methodVisitor.visitVarInsn(ALOAD, 2);  // 0 = this, 1 = receiver, 2 = class method found in.
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SUPER_SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[1]);
		} else {
			current.methodVisitor.visitInsn(ACONST_NULL);
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[1]);
		}
	}

	public void keywordSend(String keywords, int argumentCount, int line, boolean sendIsToSuper) {
		if (argumentCount > MAXIMUM_KEYWORD_ARGUMENTS)
			throw new IllegalArgumentException("More than " + MAXIMUM_KEYWORD_ARGUMENTS + " keyword arguments!");
		visitLine(line);
		current.methodVisitor.visitLdcInsn(keywords);
		if (sendIsToSuper) {
			current.methodVisitor.visitVarInsn(ALOAD, 2);  // 0 = this, 1 = receiver, 2 = class method found in.
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SUPER_SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[argumentCount]);
		} else {
			current.methodVisitor.visitInsn(ACONST_NULL);
			current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, SEND_METHOD_NAME, SEND_METHOD_DESCRIPTORS[argumentCount]);
		}
	}

	public void callToPrimitiveByNumber(int containingMethodArgumentCount, int containingMethodTemporariesCount, String number, int line) {
		visitLine(line);
		pushMethodArguments(containingMethodArgumentCount);
		pushNulls(MAXIMUM_KEYWORD_ARGUMENTS - containingMethodArgumentCount);
		current.methodVisitor.visitMethodInsn(INVOKESTATIC, current.fullyQualifiedName, "primitive_"+number, APPLY_METHOD_DESCRIPTORS[10]);
		// TODO.JCL - cater for case where primitive fails - for now return primitive result.
		current.methodVisitor.visitInsn(ARETURN);
	}

	private void pushMethodArguments(int countOfArgumentsToPush) {
		for (int i = 0; i < countOfArgumentsToPush + 2; i++)
			current.methodVisitor.visitVarInsn(ALOAD, (i + 1));
	}

	private void pushNulls(int countOfNullsToPush) {
		for (int i = 0; i < countOfNullsToPush; i++)
			current.methodVisitor.visitInsn(ACONST_NULL);
	}

	private void pushNumericValue(int value) {
		switch (value) {
			case 0: current.methodVisitor.visitInsn(ICONST_0); break;
			case 1: current.methodVisitor.visitInsn(ICONST_1); break;
			case 2: current.methodVisitor.visitInsn(ICONST_2); break;
			case 3: current.methodVisitor.visitInsn(ICONST_3); break;
			case 4: current.methodVisitor.visitInsn(ICONST_4); break;
			case 5: current.methodVisitor.visitInsn(ICONST_5); break;
			default:
				if (value > 5 && value < 128)
					current.methodVisitor.visitIntInsn(BIPUSH, value);
				else // SIPUSH not supported yet.
					throw new IllegalStateException("push of integer value " + value + " not yet supported.");
		}
	}

	public void initialize() {
		initialize(verboseOn ? tracingClassWriter() : nonTracingClassWriter());
	}

	private ClassWriter nonTracingClassWriter() {
		return new ClassWriter(ClassWriter.COMPUTE_MAXS);
	}

	private ClassWriter tracingClassWriter() {
		return new TracingClassWriter(ClassWriter.COMPUTE_MAXS);
	}

	void initialize(ClassWriter classWriter) {
		current.classWriter = classWriter;
	}

	protected void cloneContext() {
		current.storeOn(contexts);
		current = current.copy();
	}

	protected void openContext() {
		current.storeOn(contexts);
		current = new Context();
		initialize();
	}

	protected void closeContext() {
		current = current.restoreFrom(contexts);
	}

	static class Context {
		ClassWriter classWriter;
		String className;
		String sourceName;
		String packageName;
		String fullyQualifiedName;
		String sourceFileExtension;
		MethodVisitor methodVisitor;

		Context copy() {
			Context clone = new Context();
			clone.classWriter = classWriter;
			clone.className = className;
			clone.sourceName = sourceName;
			clone.packageName = packageName;
			clone.fullyQualifiedName = fullyQualifiedName;
			clone.sourceFileExtension = sourceFileExtension;
			clone.methodVisitor = methodVisitor;
			return clone;
		}

		void storeOn(Stack<Context> contexts) {
			contexts.push(this);
		}

		Context restoreFrom(Stack<Context> contexts) {
			if (contexts.isEmpty())
				return this;
			return contexts.pop();
		}
	}
}