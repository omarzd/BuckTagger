See [LICENSE](LICENSE) for licensing information.

BuckTagger
==========

BuckTagger is a rule-based classifier of Arabic words into the 
morphological categories of Buckwalter.
The goal of this project is to simplify, and to some extent automate, data entry 
into the lexicon of Buckwalter's Arabic Morphological Analyzer (BAMA).

Technical Report
----------------

A detailed technical report is [available](BuckTaggerTechnicalReportArabic.pdf) in Arabic explaining the work, its logic and design steps from theory to implementation.

Acknowledgement
---------------

Credit is due to my friend and colleague Ahmed Aman who was a big support during the long and challenging development of this project.

General Notes
-------------

The main inquiry function is called `getPrimaryTag`.
It might return more than one tag. if so, only one should be chosen to 
send to the next function `getSecondaryTags`.
The user will help choose only one primary tag of course since there 
isn't a more accurate inquiry at the moment.
The role of `getPrimaryTag` to that of `getSecondaryTags` is like 
morphological analysis to morphological generation.
More about the classification of tags into primary and secondary can 
be found in the excel file [`TagsClassified.xlsx`](TagsClassified.xlsx) in comments.

	Number of stems for each class of tags
	N : 49213 Stems
	PV: 17367 Stems
	IV: 14387 Stems
	FW: 1147  Stems
	CV: 36    Stems


Terminology
-----------

The morphological categories that were first introduced in the lexicon 
of BAMA 1.0 are called tags.
There are two types of tags, primary and secondary:
- Primary tags are those that represent the words in their original 
form, i.e., without modification. 
- Secondary tags those that represent a modified version of a word 
that is under a primary tag. Under each primary tag, fall secondary 
tags, either one, multiple or none at all.

Sample Output
-------------

In case you can't come up with suitable input, we have assembled this 
list which covers most cases. Try them for input:
- يَمضِي
- يَرعى
- يَستعمل
- يُؤذن
- يَخشى
- يُؤذِن
- يُؤذَن

For example if we take the verb يَخشَى as input, the output will be as 
follows:

	Primary Tag:
	IV_0:	 خشَى
	Secondary Tags: 
	[IV_Ann:	 خشي
	, IV_0hwnyn:	 خش
	, IV_h:	 خشا
	]

Files
-----

- [`TagsComments.xlsx`](TagsComments.xlsx) :
	In this file, a thorough study was made on each tag in the 
	Buckwalter tagset. A comprehensive description was written 
	for each tag in explicit terms defining clear attributes. 
	This came in handy at the next stage where clear lines 
	between attributes helped divide them into a table.
	Also, many notes concerning the tagset&mdash;in terms of 
	classification, coverage, and richness&mdash;and concerning 
	individual stems were written in the file.

- [`TagsClassified.xlsx`](TagsClassified.xlsx) :
	In this file, we extended our study of the Buckwalter tags 
	to be able to divide the description of each tag into an 
	orderly set of attributes, found in sheets `IV Class` and 
	`PV Class`. Only IV (Imperfect Verbs) tags and PV (Perfect 
	Verb) tags were considered for this process since they are 
	the trickiest, the most rewarding, and they make more than 
	60% of the total tags.
	
	The attributes, although very orderly, were still so 
	humanly. A lower level version needed to be done in order 
	to easily translate into a low-level decision table. 
	Moreover, they did not arrange families of tags together.
	
	Thus, the sheet `IV Class^2` was created. In it, the tags 
	were divided into primary and seconary tags. Each family 
	of tags resides under one primary tag, while the rest of 
	that family were secondary tags, i.e., children of the 
	primary tag. More about the classification of tags into 
	primary and secondary can be found in the excel file 
	[`TagsClassified.xlsx`](TagsClassified.xlsx) (hint: look for hidden comments in 
	cells).
	
	`IV Class^2` was then traslated into the lower-level 
	desicion table found in [`BuckTagsRules.xls`](BuckTagger/src/main/rules/BuckTagsRules.xls). 
	
- [`BuckTagsRules.xls`](BuckTagger/src/main/rules/BuckTagsRules.xls) :
	Attributes, defined above, were assigned to Java 
	functions. Some were assigned to two different functions 
	and took two columns in the desicion table where they had 
	only one in previous higher-level tables.


How to Compile
--------------

To compile from source, you first need to setup the environment. To do 
so, follow these steps:
1. Download and install JDK, if you don't already have it.
2. Download Eclipse IDE (the Classic version) from 
http://www.eclipse.org/downloads/. 
3. Eclipse does not need installation. It's portable. Just unzip it 
anywhere, and run eclipse.exe.
4. In Eclipse, go to "Help" -> "Install New Software" -> "Add"
5. paste the url: "http://www.jboss.org/drools/downloads.html"
6. Select the added link from the dropdown list.
7. Check "Drools and jBPM".
8. Start downloading. This will install Drools into Eclipse.
9. Restart Eclipse.
10. In Eclipse, Import the project (choosing the folder "BuckTagger") 
into eclipse.
11. Go to the menu: Project -> Properties -> Drools -> Configure 
Workspace Settings (a link on the upper-right corner) -> Add -> Create 
a new Drools 5 Runtime -> select a folder of your choice where the 
Drools runtime files will be stored.
12. Go to the menu: Project -> Properties -> Drools -> Enable project 
specific settings (the check box) -> select the runtime you just added.
13. Run the MainFrame class.

If you stopped at step 6, i.e., you can't download from the update 
site, and you can't configure eclipse's internet access, then do it 
manually. Download the update site from the same link to your PC, then 
give Eclipse its path on your PC instead of the url. Then you can 
download the runtime from http://www.jboss.org/drools/downloads.html 
(the first file named "Drools") and add it to the project's class 
path. The eclispe plugin, which is at that same download page, is the file named 
"Drools and jBPM tools". Online tutorials for installing the eclipse 
plugin are available.
	
How to Use
----------

In the input text field, enter a word. Your input must be:
- an Arabic word of course.
- an imperfect verb (in the future, all kinds of Arabic words should 
be supported).
- free of clitics and/or inflections.

It is preferable that you diacritize three letters: the first letter, 
which is the imperfective letter حرف المضارعة; the second letter, 
which is first in the root; and the before-last letter, which is 
before last in the root as well and specifically the second in a 
three-lettered root.

After you are done typing, hit Enter or Tab. As soon as you do that, 
the values of the checkboxes should change according to what the 
program has automatically estimated. Revise the values, correct 
mistakes, and then hit Enter again or click the button.
The results should look more or less like the sample output provided 
in this readme under [Sample Output](#sample-output).

How it Works
------------

When a word is entered, as soon as focus leaves the input text field, 
special methods are called to estimate values of variables of the 
word. The estimation methods and the variables are part of the class 
`VocStem` that the input word is an instance object of. Some of the 
variables are shown to the user so that the user can edit them. When 
the user changes one of the variables, s/he will override the 
previously estimated value. Finally, when the user hits the button, 
the rule engine is called to load and run the rules. The rules' 
conditions directly relate to the variables of the `VocStem` object. The 
rules' actions give the appropriate tags and the corresponding 
inflected/modified forms of the word if any.

Future Work
-----------

Primary-tag and secondary-tag decision tables were done only for IV 
types. The rest shall be completed. The comments in the excel files 
serve as a guide to writing the decision tables.
Along with that, the Java classes should be ordered into a hierarchy 
that makes sense with respect to the logical classification of the 
tags. For example, `VocStem` can be the main class, `VocVerb` would 
inherit it, and `VocPV` and `VocIV` inherit `VocVerb`. Of course the more 
specific the class, the more specific its methods should be. So the 
method `isTransitive()` should be in `VocVerb` while the method `isImpYa()` 
should be in `VocIV` beacuse it concerns the letter specific to 
Imperfect Verbs (حرف المضارعة).
Then the advanced algorithms of NLP should be introduced to reduce the 
need for a human agent.
All these suggestions are marked in TODOs in the code.

Contact
-------

For any issues or suggestions, please [create a new issue](http://github.com/omarzd/BuckTagger/issues/new).
