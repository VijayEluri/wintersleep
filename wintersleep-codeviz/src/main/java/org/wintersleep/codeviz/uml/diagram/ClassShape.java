package org.wintersleep.codeviz.uml.diagram;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.wintersleep.codeviz.uml.model.ModelClass;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.Node;
import org.wintersleep.graphviz.html.*;

import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassShape {

    private final ModelClass modelClass;
    private final ClassDrawingSettings settings;

    public ClassShape(ModelClass modelClass, ClassDrawingSettings settings) {
        if (modelClass == null) {
            throw new IllegalArgumentException();
        }
        this.modelClass = modelClass;
        this.settings = settings;
    }

    public ModelClass getModelClass() {
        return modelClass;
    }

    public ClassDrawingSettings getSettings() {
        return settings;
    }

    public String getName() {
        // TODO choose between full/simple name
        return modelClass.getClazz().getSimpleName();
    }

    public Node draw(DiGraph g) {
        TableDocument label = TableDocument.Factory.newInstance();
        Table t = label.addNewTable();
        t.setBorder(0);
        t.setCellborder(1);
        t.setCellspacing(0);
        t.setCellpadding(2);
        t.setPort("p");

        fillClassSection(addInnerTable(t));
        addAttributes(addInnerTable(t));
        addOperations(addInnerTable(t));

        //System.out.println(getName() + ": " + label);        

        //setText(t.addNewTr().addNewTd(), "operation1");
        Node n2 = g.addNode(getName());
        n2.addAttributeList()
                .setLabel(label)
                .setShape("plaintext");
        return n2;
    }

    private Table addInnerTable(Table outerTable) {
        Table innerTable = outerTable.addNewTr().addNewTd().addNewTable();
        innerTable.setBorder(0);
        innerTable.setCellspacing(0);
        innerTable.setCellpadding(1);
        return innerTable;
    }

    private void fillClassSection(Table t) {
/*
        tableLine(t, Alignment.CENTER, guilWrap("repository"));
*/
        for (String stereotype : modelClass.getStereoTypes()) {
            tableLine(t, Alignment.CENTER, guilWrap(stereotype), null);            
        }
        tableLine(t, Alignment.CENTER, " " + getName() + " ", null);
    }

    private void addAttributes(Table t) {
        Field[] fields = modelClass.getClazz().getDeclaredFields();
        if (settings.isDrawingAttributes() && fields.length > 0) {
            for (Field field : fields) {
                String line = field.getName();
                if (settings.isDrawingAttributeTypes()) {
                    line += ": " + DiagramUtil.getPresentation(field.getType());
                }
                tableLine(t, Alignment.LEFT, line, null);
            }
        } else {
            tableLine(t, Alignment.LEFT, "\n", null);
        }
    }

    private void addOperations(Table t) {
        Method[] methods = modelClass.getClazz().getMethods();
        if (settings.isDrawingOperations() && methods.length > 0) {
            for (Method method : methods) {
                tableLine(t, Alignment.LEFT, method.getName(), null);
            }
        } else {
            // must write a non-empty string here, because with an empty string,
            // xmlbeans generates <td/> instead of <td></td>, which confuses dot.
            // we write a newline instead of a space, because the newline gives
            // an inner table with a lower height, which looks better.
            tableLine(t, Alignment.LEFT, "\n", null);
        }
    }

    private void tableLine(Table t, Alignment.Enum align, String text, Font font) {
        Cell td = t.addNewTr().addNewTd();
        //final String linePostfix="\n";
        final String linePostfix = "";

        if (align != null) {
            switch (align.intValue()) {
                case Alignment.INT_CENTER:
                    td.setAlign(ExtendedAlignment.CENTER);
                    break;
                case Alignment.INT_LEFT:
                    td.setAlign(ExtendedAlignment.LEFT);
                    break;
                case Alignment.INT_RIGHT:
                    td.setAlign(ExtendedAlignment.RIGHT);
                    break;
            }
            td.setBalign(align);
        }

        if (font != null) {
            org.wintersleep.graphviz.html.Font fontElement = td.addNewFont();
            fontElement.setFace(font.getName());
            fontElement.setPointSize(10);
            setText(fontElement, text + linePostfix);
        } else {
            setText(td, text + linePostfix);
        }
    }

    public void setText(XmlObject xmlObject, String txt) {
        XmlCursor cursor = xmlObject.newCursor();
        cursor.toFirstContentToken();
        cursor.insertChars(txt);
        cursor.dispose();
    }

    private String guilWrap(String str) {
        // if we use the xml variants, then xmlbeans will escape it, so we have to use the unicode chars (for now)
        //String guilOpen = "&laquo;"; // "\u00ab";
        //String guilClose = "&raquo;"; // "\u00bb";
        String guilOpen = "\u00ab";
        String guilClose = "\u00bb";
        return guilOpen + str + guilClose;
    }

}
