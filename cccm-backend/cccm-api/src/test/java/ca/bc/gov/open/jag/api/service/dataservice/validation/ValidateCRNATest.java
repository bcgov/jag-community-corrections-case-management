package ca.bc.gov.open.jag.api.service.dataservice.validation;

import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.api.service.ValidationServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

@QuarkusTest
public class ValidateCRNATest {

    private static final String DATA_ONE = "{\n" +
            "    \"data\": {\n" +
            "        \"S02Q01\": \"A\",\n" +
            "        \"S02Q01_COMMENT\": \"update my comments please aasdfsdf\",\n" +
            "        \"S02Q01_intervention_checkbox\": true,\n" +
            "        \"S02Q02\": \"A\",\n" +
            "        \"S02Q02_COMMENT\": \"<ul><li>comments for living arrangements, updated on Oct 20</li><li>sdflsdaf</li><li>sfa</li></ul>\",\n" +
            "        \"S02Q02_intervention_checkbox\": false,\n" +
            "        \"S02Q03\": \"C\",\n" +
            "        \"S02Q03_COMMENT\": \"\",\n" +
            "        \"S02Q03_intervention_checkbox\": false,\n" +
            "        \"S02Q04\": \"D\",\n" +
            "        \"S02Q04_COMMENT\": \"\",\n" +
            "        \"S02Q04_intervention_checkbox\": false,\n" +
            "        \"S02Q05\": \"\",\n" +
            "        \"S02Q05_COMMENT\": \"\",\n" +
            "        \"S02Q05_intervention_checkbox\": false,\n" +
            "        \"S02Q06\": \"D\",\n" +
            "        \"S02Q06_COMMENT\": \"\",\n" +
            "        \"S02Q06_intervention_checkbox\": false,\n" +
            "        \"S02Q07\": \"B\",\n" +
            "        \"S02Q07_COMMENT\": \"\",\n" +
            "        \"S02Q07_intervention_checkbox\": false,\n" +
            "        \"S02Q08\": \"C\",\n" +
            "        \"S02Q08_COMMENT\": \"\",\n" +
            "        \"S02Q08_intervention_checkbox\": false,\n" +
            "        \"S02Q09\": \"\",\n" +
            "        \"S02Q09_COMMENT\": \"\",\n" +
            "        \"S02Q09_intervention_checkbox\": false,\n" +
            "        \"S01Q02\": \"H\",\n" +
            "        \"S01Q02_COMMENT\": \"overall needs rating, updates.\",\n" +
            "        \"S02Q10\": \"aaaa\",\n" +
            "        \"S03Q01\": \"\",\n" +
            "        \"S03Q01_COMMENT\": \"\",\n" +
            "        \"S03Q02\": \"\",\n" +
            "        \"S03Q02_COMMENT\": \"\",\n" +
            "        \"S03Q03\": \"\",\n" +
            "        \"S03Q03_COMMENT\": \"\",\n" +
            "        \"S03Q04\": \"\",\n" +
            "        \"S03Q04_COMMENT\": \"\",\n" +
            "        \"S03Q05\": \"\",\n" +
            "        \"S03Q05_COMMENT\": \"\",\n" +
            "        \"S03Q06\": \"\",\n" +
            "        \"S03Q06_COMMENT\": \"\",\n" +
            "        \"S03Q07\": \"\",\n" +
            "        \"S03Q07_COMMENT\": \"\",\n" +
            "        \"S03Q08\": \"\",\n" +
            "        \"S03Q08_COMMENT\": \"\",\n" +
            "        \"S03Q09\": \"\",\n" +
            "        \"S03Q09_COMMENT\": \"\",\n" +
            "        \"S03Q10\": \"\",\n" +
            "        \"S03Q10_COMMENT\": \"\",\n" +
            "        \"S03Q11\": \"\",\n" +
            "        \"S01Q03\": \"M\",\n" +
            "        \"S01Q03_COMMENT\": \"<p>overall risk rating updates again</p>\",\n" +
            "        \"S01Q01\": \"L\",\n" +
            "        \"S01Q01_COMMENT\": \"<p>supervision rating updates.</p>\",\n" +
            "        \"S02Q01_intervention_datagrid\": [\n" +
            "            {\n" +
            "                \"S02Q01_intervention_desc\": \"aaaaaaa bbb ccc\",\n" +
            "                \"S02Q01_intervention_type\": \"EMPA\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"S02Q01_intervention_desc\": \"aasdfsdfsdafsdaf\",\n" +
            "                \"S02Q01_intervention_type\": \"FARE\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"responsivity\": {\n" +
            "            \"PRRE\": true,\n" +
            "            \"PRRE_desc\": \"previous responses to dsfsdf...\",\n" +
            "            \"BACO\": true,\n" +
            "            \"MHCF\": true,\n" +
            "            \"MHCF_desc\": \" safsdfsdafsdafsdfasdf\",\n" +
            "            \"BACO_desc\": \"Barriers\"\n" +
            "        },\n" +
            "        \"PLAN_SUMMARY_TXT\": \"sdfsdfasd 1\",\n" +
            "        \"COMMENT_TXT\": \"reassessmnt asdsfsd 1\"\n" +
            "    },\n" +
            "    \"clientFormId\": 389821\n" +
            "}";

    private static final String DATA_TWO = "{\n" +
            "    \"S02Q01\": \"A\",\n" +
            "    \"S02Q01_COMMENT\": \"update my comments please aasdfsdf\",\n" +
            "    \"S02Q01_intervention_checkbox\": true,\n" +
            "    \"S02Q02\": \"C\",\n" +
            "    \"S02Q02_COMMENT\": \"<ul><li>comments for living arrangements, updated on Oct 20</li><li>sdflsdaf</li><li>sfa</li></ul>\",\n" +
            "    \"S02Q02_intervention_checkbox\": false,\n" +
            "    \"S02Q03\": \"C\",\n" +
            "    \"S02Q03_COMMENT\": \"\",\n" +
            "    \"S02Q03_intervention_checkbox\": false,\n" +
            "    \"S02Q04\": \"D\",\n" +
            "    \"S02Q04_COMMENT\": \"\",\n" +
            "    \"S02Q04_intervention_checkbox\": false,\n" +
            "    \"S02Q05\": \"\",\n" +
            "    \"S02Q05_COMMENT\": \"\",\n" +
            "    \"S02Q05_intervention_checkbox\": false,\n" +
            "    \"S02Q06\": \"D\",\n" +
            "    \"S02Q06_COMMENT\": \"\",\n" +
            "    \"S02Q06_intervention_checkbox\": false,\n" +
            "    \"S02Q07\": \"B\",\n" +
            "    \"S02Q07_COMMENT\": \"\",\n" +
            "    \"S02Q07_intervention_checkbox\": false,\n" +
            "    \"S02Q08\": \"C\",\n" +
            "    \"S02Q08_COMMENT\": \"\",\n" +
            "    \"S02Q08_intervention_checkbox\": false,\n" +
            "    \"S02Q09\": \"\",\n" +
            "    \"S02Q09_COMMENT\": \"\",\n" +
            "    \"S02Q09_intervention_checkbox\": false,\n" +
            "    \"S01Q02\": \"H\",\n" +
            "    \"S01Q02_COMMENT\": \"overall needs rating, updates.\",\n" +
            "    \"S02Q10\": \"aaaa\",\n" +
            "    \"S03Q01\": \"\",\n" +
            "    \"S03Q01_COMMENT\": \"\",\n" +
            "    \"S03Q02\": \"\",\n" +
            "    \"S03Q02_COMMENT\": \"\",\n" +
            "    \"S03Q03\": \"\",\n" +
            "    \"S03Q03_COMMENT\": \"\",\n" +
            "    \"S03Q04\": \"\",\n" +
            "    \"S03Q04_COMMENT\": \"\",\n" +
            "    \"S03Q05\": \"\",\n" +
            "    \"S03Q05_COMMENT\": \"\",\n" +
            "    \"S03Q06\": \"\",\n" +
            "    \"S03Q06_COMMENT\": \"\",\n" +
            "    \"S03Q07\": \"\",\n" +
            "    \"S03Q07_COMMENT\": \"\",\n" +
            "    \"S03Q08\": \"\",\n" +
            "    \"S03Q08_COMMENT\": \"\",\n" +
            "    \"S03Q09\": \"\",\n" +
            "    \"S03Q09_COMMENT\": \"\",\n" +
            "    \"S03Q10\": \"\",\n" +
            "    \"S03Q10_COMMENT\": \"\",\n" +
            "    \"S03Q11\": \"\",\n" +
            "    \"S01Q03\": \"M\",\n" +
            "    \"S01Q03_COMMENT\": \"<p>overall risk rating updates again</p>\",\n" +
            "    \"S01Q01\": \"L\",\n" +
            "    \"S01Q01_COMMENT\": \"<p>supervision rating updates.</p>\",\n" +
            "    \"S02Q01_ID\": 12359794,\n" +
            "    \"S02Q01_intervention_datagrid\": [\n" +
            "        {\n" +
            "            \"S02Q01_intervention_id\": 281,\n" +
            "            \"S02Q01_intervention_desc\": \"\",\n" +
            "            \"S02Q01_intervention_type\": \"EMPA\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"S02Q01_intervention_id\": 282,\n" +
            "            \"S02Q01_intervention_desc\": \"aasdfsdfsdafsdaf s\",\n" +
            "            \"S02Q01_intervention_type\": \"OTHR\",\n" +
            "            \"S02Q01_intervention_specify\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"S02Q01_intervention_id\": 283,\n" +
            "            \"S02Q01_intervention_desc\": \"\",\n" +
            "            \"S02Q01_intervention_type\": \"OTHR\",\n" +
            "            \"S02Q01_intervention_specify\": \"text\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"S02Q02_ID\": 12359793,\n" +
            "    \"S02Q03_ID\": 12359795,\n" +
            "    \"S02Q04_ID\": 12359796,\n" +
            "    \"S02Q05_ID\": 12359899,\n" +
            "    \"S02Q06_ID\": 12359898,\n" +
            "    \"S02Q07_ID\": 12359900,\n" +
            "    \"S02Q08_ID\": 12359901,\n" +
            "    \"S01Q02_ID\": 12359950,\n" +
            "    \"S02Q10_ID\": 12359930,\n" +
            "    \"S01Q03_ID\": 12359951,\n" +
            "    \"S01Q01_ID\": 12359952,\n" +
            "    \"COMMENT_TXT\": \"reassessmnt asdsfsd 1\",\n" +
            "    \"responsivity\": {\n" +
            "        \"PRRE\": true,\n" +
            "        \"PRRE_desc\": \"previous responses to dsfsdf...\",\n" +
            "        \"BACO\": true,\n" +
            "        \"MHCF\": true,\n" +
            "        \"MHCF_desc\": \" safsdfsdafsdafsdfasdf\",\n" +
            "        \"BACO_desc\": \"Barriers\"\n" +
            "    },\n" +
            "    \"PLAN_SUMMARY_TXT\": \"sdfsdfasd 1\"\n" +
            "}";

    @Inject
    ValidationService sut;

    @Test
    @DisplayName("Success: should validate data")
    public void testValidations() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCRNA(DATA_ONE);

        Assertions.assertEquals(15, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsTwo() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCRNA(DATA_TWO);

        Assertions.assertEquals(18, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmptyJson() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCRNA("{}");

        Assertions.assertEquals(21, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmpty() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCRNA("");

        Assertions.assertEquals(21, result.getErrors().size());

    }


}
