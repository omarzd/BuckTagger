// This class contains static methods related to the core processing.

package com.amt;

import java.util.ArrayList;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;


@SuppressWarnings("unused")
public class Core {
	protected static KnowledgeBase globalKbase;

	// removes the imperfective letter from the beginning of the word
	// danger: assuming it is an imperfect verb
	public static String removeImp(String s) { 
		for (String  l : VocStem.IMP_LETTERS) {
			if(s.indexOf(l) == 0) {
				s = s.substring(1);
				break;
			}
		}
		if(s.charAt(0) == VocStem.FATHA.charAt(0) || s.charAt(0) == VocStem.DAMMA.charAt(0))
			s = s.substring(1);
		return s;
	}
	
	// this should not be used unless the process is intended to be fully automatic, i.e., you do not need to manually tweak fields of the VocStem object.
	public static ArrayList<TagStem> doMagic(String inputString) {
		return doMagic(new VocStem(inputString));
	}

	// instead of reading the knowledge base every time doMagic is called, it should be done only once
	public static void readRules() {
		try {
			globalKbase = readKnowledgeBase();		
		} catch (Throwable t) {
            t.printStackTrace();
        }
	}

	// this is the method that does everything. Input and return type say it all.
	public static ArrayList<TagStem> doMagic(VocStem input) {
        try {
            // load up the knowledge base which contains the rules
            StatefulKnowledgeSession ksession = globalKbase.newStatefulKnowledgeSession();
            
            // setup logger for debugging. Remember to logger.close();
            //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");	
 
            // insert input
            ksession.insert(input);
            
            // go
            ksession.fireAllRules();
            
            // before second call, clear the secondary tag list
            input.setSecondaryTagStems(new ArrayList<TagStem>());
            
            // Do again to get secondary tags
            StatefulKnowledgeSession ksession2 = globalKbase.newStatefulKnowledgeSession();
            ksession2.insert(input);
            ksession2.fireAllRules();

            // end
			//logger.close();
	        return input.getSecondaryTagStems();
        } catch (Throwable t) {
            t.printStackTrace();
        }		
        // this should not be reached
        return input.getSecondaryTagStems();
		
	}

	// the following method reads the decision table into a knowledgebase
	// It uses Drools 5.0 API.
    private static KnowledgeBase readKnowledgeBase() throws Exception {   
        // setup config
        DecisionTableConfiguration dtConfig = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtConfig.setInputType(DecisionTableInputType.XLS);
        KnowledgeBuilderConfiguration kbConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();    
        kbConfig.setProperty("drools.dialect.mvel.strict", "false");

        // setup builder
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbConfig);        
        kbuilder.add(ResourceFactory.newClassPathResource("BuckTagsRules.xls"), ResourceType.DTABLE, dtConfig);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            //throw new IllegalArgumentException("Could not parse knowledge.");
        }
        
        // setup and return base
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }  

}
