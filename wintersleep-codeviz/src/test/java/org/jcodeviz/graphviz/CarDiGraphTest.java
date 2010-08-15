package org.jcodeviz.graphviz;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.jcodeviz.graphviz.html.*;
import org.jcodeviz.test.FileTestUtil;
import static org.jcodeviz.test.FileTestUtil.assertCreated;
import org.junit.Test;

import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

public class CarDiGraphTest {

    private final File outputDir = FileTestUtil.makeOutputDir(CarDiGraphTest.class);

    @Test
    public void test() throws IOException, InterruptedException, XmlException {
        DiGraph g = new DiGraph("CarDiGraph");
        g.setMessage("line 1", "line 2");
        g.addEdgeAttributeListList().addAttributeList()
                .setFontName("Helvetica")
                .setFontSize(10)
                .setLabelFontName("Helvetica")
                .setLabelFontSize(10);
        g.addNodeAttributeListList().addAttributeList()
                .setFontName("Helvetica")
                .setFontSize(10)
                .setShape("plaintext");
        boolean horizontal = true;
        if (horizontal) {
            g.getAttributeList()
                    .setRankDir(RankDir.LR)
                    .setRankSep(1);
        }
        Color bgColor = Color.blue;
        if (bgColor != null) {
            //g.getAttributeList().setBgColor(bgColor);
            g.getAttributeList().setBgColor("red");
        }
        Node n1 = addClassNode(g, "Car");
        Node n2 = addClassNode(g, "Wheel");

        g.addEdge(n1, "p", n2, "p").addAttributeList()
                //.setDir(Direction.BACK)
                .setStyle(EdgeStyle.SOLID)
                .setArrowTail(Arrow.DIAMOND)
                .setTailLabel("0..1")
                .setLabel("label")
                .setArrowHead(Arrow.NONE)
                .setHeadLabel("0..*")
                        //.setFontName("helvetica")
                .setFontColor(Color.RED)
                .setFontSize(10.0)
                        //.setLabelAngle(30)
                .setLabelDistance(2.0);


//        PrintWriter w = new PrintWriter(System.out);
//        g.print(w);
//        w.flush();
//        w.close();

        assertCreated(g.makeImageFile(outputDir, "png", true));
    }

    private Node addClassNode(DiGraph g, final String className) {
        TableDocument label = TableDocument.Factory.newInstance();
        Table t = label.addNewTable();
        t.setBorder(0);
        t.setCellborder(1);
        t.setCellspacing(0);
        t.setCellpadding(2);
        t.setPort("p");

        fillClassSection(t.addNewTr().addNewTd().addNewTable(), className);
        setText(t.addNewTr().addNewTd(), "attribute1");
        setText(t.addNewTr().addNewTd(), "operation1");
        Node n2 = g.addNode(className);
        n2.addAttributeList()
                .setLabel(label)
                .setShape("plaintext");
        return n2;
    }

    private void fillClassSection(Table t, final String className) {
        t.setBorder(0);
        t.setCellspacing(0);
        t.setCellpadding(1);
/*
        tableLine(t, Alignment.CENTER, guilWrap("repository"));
        tableLine(t, Alignment.CENTER, guilWrap("singleton"));
*/
        tableLine(t, Alignment.CENTER, " " + className + " ");
    }

    private String guilWrap(String str) {
        // if we use the xml variants, then xmlbeans will escape it, so we have to use the unicode chars (for now)
        //String guilOpen = "&laquo;"; // "\u00ab";
        //String guilClose = "&raquo;"; // "\u00bb";
        String guilOpen = "\u00ab";
        String guilClose = "\u00bb";
        return guilOpen + str + guilClose;
    }


    private void tableLine(Table t, Alignment.Enum align, String text) {
        tableLine(t, align, text, null);
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
            org.jcodeviz.graphviz.html.Font fontElement = td.addNewFont();
            fontElement.setFace(font.getName());
            fontElement.setPointSize(10);
            setText(fontElement, text + linePostfix);
        } else {
            setText(td, text + linePostfix);
        }
    }

/*
    private String fontWrap(String text, Options opt, Font font) {
        if (font == Font.ABSTRACT) {
            return fontWrap(text, opt.nodeFontAbstractName, opt.nodeFontSize);
        } else if (font == Font.CLASS) {
            return fontWrap(text, opt.nodeFontClassName, opt.nodeFontClassSize);
        } else if (font == Font.CLASS_ABSTRACT) {
            String name;
            if (opt.nodeFontClassAbstractName == null)
                name = opt.nodeFontAbstractName;
            else
                name = opt.nodeFontClassAbstractName;
            return fontWrap(text, name, opt.nodeFontClassSize);
        } else if (font == Font.PACKAGE) {
            return fontWrap(text, opt.nodeFontPackageName, opt.nodeFontPackageSize);
        } else if (font == Font.TAG) {
            return fontWrap(text, opt.nodeFontTagName, opt.nodeFontTagSize);
        } else {
            return text;
        }
    }
*/

    /**
     * Wraps the text with the appropriate font tags when the font name
     * and size are not void
     *
     * @param text     the text to be wrapped
     * @param fontName considered void when it's null
     * @param fontSize considered void when it's <= 0
     */
    private String fontWrap(String text, String fontName, double fontSize) {
        if (fontName == null && fontSize == -1)
            return text;
        else if (fontName == null)
            return "<font point-size=\"" + fontSize + "\">" + text + "</font>";
        else if (fontSize <= 0)
            return "<font face=\"" + fontName + "\">" + text + "</font>";
        else
            return "<font face=\"" + fontName + "\" point-size=\"" + fontSize + "\">" + text + "</font>";
    }


    public void setText(XmlObject xmlObject, String txt) {
        XmlCursor cursor = xmlObject.newCursor();
        cursor.toFirstContentToken();
        cursor.insertChars(txt);
        cursor.dispose();
    }

    public static void main(String[] args) {

        try {
            new CarDiGraphTest().test();
            //System.out.println("hello");
            ImageViewer viewer = new ImageViewer("/tmp/G1.png");
            viewer.view();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
