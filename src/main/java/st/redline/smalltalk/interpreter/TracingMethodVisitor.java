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
package st.redline.smalltalk.interpreter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracingMethodVisitor implements MethodVisitor, Opcodes {

	final Logger log = LoggerFactory.getLogger(TracingMethodVisitor.class);

	private final MethodVisitor methodVisitor;

	public TracingMethodVisitor(MethodVisitor methodVisitor) {
		this.methodVisitor = methodVisitor;
	}

	public AnnotationVisitor visitAnnotationDefault() {
		log.info("visitAnnotationDefault()");
		return methodVisitor.visitAnnotationDefault();
	}

	public AnnotationVisitor visitAnnotation(String s, boolean b) {
		log.info("visitAnnotation('" + s + "', " + b + ")");
		return methodVisitor.visitAnnotation(s, b);
	}

	public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
		log.info("visitParameterAnnotation(" + i + ", '" + s + "', " + b + ")");
		return methodVisitor.visitParameterAnnotation(i, s, b);
	}

	public void visitAttribute(Attribute attribute) {
		log.info("visitAttribute(" + attribute + ")");
		methodVisitor.visitAttribute(attribute);
	}

	public void visitCode() {
		log.info("visitCode()");
		methodVisitor.visitCode();
	}

	public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
		log.info("visitFrame(" + i + ", " + i1 + ", " + objects + ", " + i2 + ", " + objects1 + ")");
		methodVisitor.visitFrame(i, i1, objects, i2, objects1);
	}

	public void visitInsn(int i) {
		log.info("visitInsn(" + opcodeAsString(i) + ")");
		methodVisitor.visitInsn(i);
	}

	public void visitIntInsn(int i, int i1) {
		log.info("visitIntInsn(" + opcodeAsString(i) + ", " + i1 + ")");
		methodVisitor.visitIntInsn(i, i1);
	}

	public void visitVarInsn(int i, int i1) {
		log.info("visitVarInsn(" + opcodeAsString(i) + ", " + i1 + ")");
		methodVisitor.visitVarInsn(i, i1);
	}

	public void visitTypeInsn(int i, String s) {
		log.info("visitTypeInsn(" + opcodeAsString(i) + ", '" + s + "')");
		methodVisitor.visitTypeInsn(i, s);
	}

	public void visitFieldInsn(int i, String s, String s1, String s2) {
		log.info("visitFieldInsn(" + opcodeAsString(i) + ", '" + s + "', '" + s1 + "', '" + s2 + "')");
		methodVisitor.visitFieldInsn(i, s, s1, s2);
	}

	public void visitMethodInsn(int i, String s, String s1, String s2) {
		log.info("visitMethodInsn(" + opcodeAsString(i) + ", '" + s + "', '" + s1 + "', '" + s2 + "')");
		methodVisitor.visitMethodInsn(i, s, s1, s2);
	}

	public void visitJumpInsn(int i, Label label) {
		log.info("visitJumpInsn(" + opcodeAsString(i) + ", " + label + ")");
		methodVisitor.visitJumpInsn(i, label);
	}

	public void visitLabel(Label label) {
		log.info("visitLabel(" + label + ")");
		methodVisitor.visitLabel(label);
	}

	public void visitLdcInsn(Object o) {
		log.info("visitLdcInsn(" + o + ")");
		methodVisitor.visitLdcInsn(o);
	}

	public void visitIincInsn(int i, int i1) {
		log.info("visitIincInsn(" + opcodeAsString(i) + ", " + i1 + ")");
		methodVisitor.visitIincInsn(i, i1);
	}

	public void visitTableSwitchInsn(int i, int i1, Label label, Label[] labels) {
		log.info("visitTableSwitchInsn(" + i + ", " + i1 + ", " + label + ", " + labels + ")");
		methodVisitor.visitTableSwitchInsn(i, i1, label, labels);
	}

	public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
		log.info("visitLookupSwitchInsn(" + label + ", " + ints + ", " + labels + ")");
		methodVisitor.visitLookupSwitchInsn(label, ints, labels);
	}

	public void visitMultiANewArrayInsn(String s, int i) {
		log.info("visitMultiANewArrayInsn('" + s + "', " + i + ")");
		methodVisitor.visitMultiANewArrayInsn(s, i);
	}

	public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
		log.info("visitTryCatchBlock(" + label + ", " + label1 + ", " + label2 + ", '" + s + "')");
		methodVisitor.visitTryCatchBlock(label, label1, label2, s);
	}

	public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
		log.info("visitLocalVariable('" + s + "', '" + s1 + "', '" + s2 + "', " + label + ", " + label1 + ", " + i + ")");
		methodVisitor.visitLocalVariable(s, s1, s2, label, label1, i);
	}

	public void visitLineNumber(int i, Label label) {
		log.info("visitLineNumber(" + i + ", " + label + ")");
		methodVisitor.visitLineNumber(i, label);
	}

	public void visitMaxs(int i, int i1) {
		log.info("visitMaxs(" + i + ", " + i1 + ")");
		methodVisitor.visitMaxs(i, i1);
	}

	public void visitEnd() {
		log.info("visitEnd()");
		methodVisitor.visitEnd();
	}

    public String opcodeAsString(int opcode) {
        switch (opcode) {
            case NOP: return "NOP";
            case ACONST_NULL: return "ACONST_NULL";
            case ICONST_M1: return "ICONST_M1";
            case ICONST_0: return "ICONST_0";
            case ICONST_1: return "ICONST_1";
            case ICONST_2: return "ICONST_2";
            case ICONST_3: return "ICONST_3";
            case ICONST_4: return "ICONST_4";
            case ICONST_5: return "ICONST_5";
            case LCONST_0: return "LCONST_0";
            case LCONST_1: return "LCONST_1";
            case FCONST_0: return "FCONST_0";
            case FCONST_1: return "FCONST_1";
            case FCONST_2: return "FCONST_2";
            case DCONST_0: return "DCONST_0";
            case DCONST_1: return "DCONST_1";
            case BIPUSH: return "BIPUSH";
            case SIPUSH: return "SIPUSH";
            case LDC: return "LDC";
            case ILOAD: return "ILOAD";
            case LLOAD: return "LLOAD";
            case FLOAD: return "FLOAD";
            case DLOAD: return "DLOAD";
            case ALOAD: return "ALOAD";
            case IALOAD: return "IALOAD";
            case LALOAD: return "LALOAD";
            case FALOAD: return "FALOAD";
            case DALOAD: return "DALOAD";
            case AALOAD: return "AALOAD";
            case BALOAD: return "BALOAD";
            case CALOAD: return "CALOAD";
            case SALOAD: return "SALOAD";
            case ISTORE: return "ISTORE";
            case LSTORE: return "LSTORE";
            case FSTORE: return "FSTORE";
            case DSTORE: return "DSTORE";
            case ASTORE: return "ASTORE";
            case IASTORE: return "IASTORE";
            case LASTORE: return "LASTORE";
            case FASTORE: return "FASTORE";
            case DASTORE: return "DASTORE";
            case AASTORE: return "AASTORE";
            case BASTORE: return "BASTORE";
            case CASTORE: return "CASTORE";
            case SASTORE: return "SASTORE";
            case POP: return "POP";
            case POP2: return "POP2";
            case DUP: return "DUP";
            case DUP_X1: return "DUP_X1";
            case DUP_X2: return "DUP_X2";
            case DUP2: return "DUP2";
            case DUP2_X1: return "DUP2_X1";
            case DUP2_X2: return "DUP2_X2";
            case SWAP: return "SWAP";
            case IADD: return "IADD";
            case LADD: return "LADD";
            case FADD: return "FADD";
            case DADD: return "DADD";
            case ISUB: return "ISUB";
            case LSUB: return "LSUB";
            case FSUB: return "FSUB";
            case DSUB: return "DSUB";
            case IMUL: return "IMUL";
            case LMUL: return "LMUL";
            case FMUL: return "FMUL";
            case DMUL: return "DMUL";
            case IDIV: return "IDIV";
            case LDIV: return "LDIV";
            case FDIV: return "FDIV";
            case DDIV: return "DDIV";
            case IREM: return "IREM";
            case LREM: return "LREM";
            case FREM: return "FREM";
            case DREM: return "DREM";
            case INEG: return "INEG";
            case LNEG: return "LNEG";
            case FNEG: return "FNEG";
            case DNEG: return "DNEG";
            case ISHL: return "ISHL";
            case LSHL: return "LSHL";
            case ISHR: return "ISHR";
            case LSHR: return "LSHR";
            case IUSHR: return "IUSHR";
            case LUSHR: return "LUSHR";
            case IAND: return "IAND";
            case LAND: return "LAND";
            case IOR: return "IOR";
            case LOR: return "LOR";
            case IXOR: return "IXOR";
            case LXOR: return "LXOR";
            case IINC: return "IINC";
            case I2L: return "I2L";
            case I2F: return "I2F";
            case I2D: return "I2D";
            case L2I: return "L2I";
            case L2F: return "L2F";
            case L2D: return "L2D";
            case F2I: return "F2I";
            case F2L: return "F2L";
            case F2D: return "F2D";
            case D2I: return "D2I";
            case D2L: return "D2L";
            case D2F: return "D2F";
            case I2B: return "I2B";
            case I2C: return "I2C";
            case I2S: return "I2S";
            case LCMP: return "LCMP";
            case FCMPL: return "FCMPL";
            case FCMPG: return "FCMPG";
            case DCMPL: return "DCMPL";
            case DCMPG: return "DCMPG";
            case IFEQ: return "IFEQ";
            case IFNE: return "IFNE";
            case IFLT: return "IFLT";
            case IFGE: return "IFGE";
            case IFGT: return "IFGT";
            case IFLE: return "IFLE";
            case IF_ICMPEQ: return "IF_ICMPEQ";
            case IF_ICMPNE: return "IF_ICMPNE";
            case IF_ICMPLT: return "IF_ICMPLT";
            case IF_ICMPGE: return "IF_ICMPGE";
            case IF_ICMPGT: return "IF_ICMPGT";
            case IF_ICMPLE: return "IF_ICMPLE";
            case IF_ACMPEQ: return "IF_ACMPEQ";
            case IF_ACMPNE: return "IF_ACMPNE";
            case GOTO: return "GOTO";
            case JSR: return "JSR";
            case RET: return "RET";
            case TABLESWITCH: return "TABLESWITCH";
            case LOOKUPSWITCH: return "LOOKUPSWITCH";
            case IRETURN: return "IRETURN";
            case LRETURN: return "LRETURN";
            case FRETURN: return "FRETURN";
            case DRETURN: return "DRETURN";
            case ARETURN: return "ARETURN";
            case RETURN: return "RETURN";
            case GETSTATIC: return "GETSTATIC";
            case PUTSTATIC: return "PUTSTATIC";
            case GETFIELD: return "GETFIELD";
            case PUTFIELD: return "PUTFIELD";
            case INVOKEVIRTUAL: return "INVOKEVIRTUAL";
            case INVOKESPECIAL: return "INVOKESPECIAL";
            case INVOKESTATIC: return "INVOKESTATIC";
            case INVOKEINTERFACE: return "INVOKEINTERFACE";
            case INVOKEDYNAMIC: return "INVOKEDYNAMIC";
            case NEW: return "NEW";
            case NEWARRAY: return "NEWARRAY";
            case ANEWARRAY: return "ANEWARRAY";
            case ARRAYLENGTH: return "ARRAYLENGTH";
            case ATHROW: return "ATHROW";
            case CHECKCAST: return "CHECKCAST";
            case INSTANCEOF: return "INSTANCEOF";
            case MONITORENTER: return "MONITORENTER";
            case MONITOREXIT: return "MONITOREXIT";
            case MULTIANEWARRAY: return "MULTIANEWARRAY";
            case IFNULL: return "IFNULL";
            case IFNONNULL: return "IFNONNULL";
            default:
                return "unknown";
        }
    }
}
