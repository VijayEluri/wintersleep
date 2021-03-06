package org.wintersleep.codeviz.uml.diagram;

import org.junit.Test;
import org.wintersleep.codeviz.uml.model.CodeModel;
import org.wintersleep.codeviz.uml.model.ModelClass;
import org.wintersleep.codeviz.uml.model.Relation;
import org.wintersleep.codeviz.uml.model.RelationEndpoint;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;

import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class JCodeVizModelTest {

    private final File outputDir = FileTestUtil.makeOutputDir(JCodeVizModelTest.class, "test-diagrams");

    @Test
    public void test() throws IOException, InterruptedException {
        CodeModel model = new CodeModel("JCodeVizModel");
        model.addClass(CodeModel.class);
        model.addClass(ModelClass.class);
        model.addClass(Relation.class);
        model.addClass(RelationEndpoint.class);
        model.addFieldRelations();

        ClassDiagram d = new ClassDiagram("JCodeVizDiagram", model);
        d.getSettings()
                .enableDrawingAttributes()
                .disableDrawingOperations();
        d.addAllModelClasses();
        d.add(CodeModel.class).getSettings()
                .enableDrawingAttributes()
                .enableDrawingAttributeTypes();
        //.enableDrawingOperations();
        assertCreated(d.createGraph().makeImageFile(outputDir, "png", true));

    }


}
