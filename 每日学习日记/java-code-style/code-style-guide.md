---
updated: 2025-07-28T17:12:55.737+08:00
edited_seconds: 3412
---
**Useful setup**
1. Settings > Tools > Actions on Save > Reformat Code, Optimize imports, Rearrange Code, Code Cleanup
2. Reformat File Dialog (Ctrl + Alt + Shift + L) > Reformat Code, Optimize imports, Rearrange Code, Code Cleanup
3. Settings > Appearance & Behavior > System Settings > Autosave > Save files if the IDE is idle for 60 seconds
4. Settings > Editor > General > Soft Wraps > Soft Wraps these files (It is helpful if you're using `Markdown`)
5. Settings > Languages & Frameworks > Markdown > Markdown Extensions (`PlantUML`) (If you're using `PlantUML`)
![](code-style-guide-1753694157737.png)
**Suggestions**
1. Use `google-java-format` plugin. Refer the official page for the setup: https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config.
2. Use the following IntelliJ-based solutions:
	1. `.editorconfig`/ `ifastIlluminatorStyle.xml`: indentation, alignment, and chain wrapping
	2. `.idea/inspectionProfiles/Project_Default.xml`: For warning code smells, security issues, and potential bugs. Currently, it will warn on `ControlFlowStatementWithoutBraces`, `MagicNumber` and `SerializableHasSerialVersionUIDField`.
	3. `.idea/codeStyles/Project.xml`: To configure Java code rearranging properties. It will help you to rearrange the class fields according to the rules specified. Currently, it will rearrange in the following sequence: Service > Validator > Dao > Repository > Mapper.
3. Use `SonarCube IDE` plugin. `SonarCube` can be used for detecting code smells, security issues, and potential bugs. Advanced features (such as toggling on/off for the whole team) will require advanced setup.


**FAQ**
- Ques: Can I use XML + `.editorconfig` together?
- Ans: Yes. The IDE will use the configuration in `.editorconfig` first, then the customized XML, then finally the IDE default settings. However, the current XML configuration is sufficient enough to cover almost all the scenarios.

- Ques: How to use XML/ `.editorconfig` in my project?
- Ans: It depends
	- `.editorconfig`: Copy it directly it to the root folder. This approach is no longer in use.
	- `ifastIlluminatorStyle.xml`: Settings > Editor > Code Style > Java > Scheme (Settings icon) > Import Scheme > IntelliJ code style XML. It should show a message like this.
	  ![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753693193867.png)
	- `.idea/inspectionProfiles/Project_Default.xml`: Copy the content of the file to the target file. If there's no, create one.
	- `.idea/codeStyles/Project.xml`: Copy the content of the file to the target file. If there's no, create one.

- Ques: Will autoformat affect my whole files? (Perhaps no)
- Ans: In IntelliJ IDEA, enabling automatic formatting on save **does not automatically reformat every file** in your project by default—it only affects files based on your settings and scope selections.

- Ques: Why it seems like the configuration didn't apply?
- Ans: The solutions depend on the your scenario:
	- For any changes in the `ifastIlluminatorStyle.xml`, you should reimport the scheme again to load the new changes.
	- For any changes in the `.editorconfig`, the changes should apply instantly.
	- If the configuration still didn't apply after all, invalidate the cache and restart the IDE. File (Menu bar) > Invalidate Caches
	  ![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753693840322.png)


- [ ] Coding Style
	- [ ] How to use Checkstyle & specify the rules?
	- [ ] How to integrate Checkstyle into the gitlab cicd?
		- [ ] How to perform testing on latest CICD safely?
	- [ ] Is there any other tools to visualize Checkstyle (since currently it's merely command line tools)
	- [ ] What are the capabilities of Spotless?


# 1. XML-General
---
## 1.1. XML File Overview

```xml
<code_scheme name="GoogleStyle">
    <!-- Completely follow Google Java Format-->
    <option name="OTHER_INDENT_OPTIONS">
        <value>
            <option name="INDENT_SIZE" value="4"/>
            <option name="CONTINUATION_INDENT_SIZE" value="4"/>
            <option name="TAB_SIZE" value="2"/>
            <option name="USE_TAB_CHARACTER" value="false"/>
            <option name="SMART_TABS" value="false"/>
            <option name="LABEL_INDENT_SIZE" value="0"/>
            <option name="LABEL_INDENT_ABSOLUTE" value="false"/>
            <option name="USE_RELATIVE_INDENTS" value="false"/>
        </value>
    </option>
    
    <option name="RIGHT_MARGIN" value="100"/>
    <!-- Keep only 1 blank lines between code -->
    <option name="KEEP_BLANK_LINES_IN_CODE" value="1"/>
    <!-- Split control statement into multiple lines -->
    <option name="KEEP_CONTROL_STATEMENT_IN_ONE_LINE" value="false"/>
    <!-- Remove any blank lines before the class end -->
    <option name="KEEP_BLANK_LINES_BEFORE_RBRACE" value="0"/>
    <option name="BLANK_LINES_AFTER_CLASS_HEADER" value="0"/>
    <option name="ALIGN_MULTILINE_PARAMETERS" value="false"/>
    <option name="ALIGN_MULTILINE_FOR" value="false"/>
    <option name="SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE" value="true"/>
    <!-- Wrap only if the code is too long -->
    <option name="CALL_PARAMETERS_WRAP" value="1"/>
    <option name="METHOD_PARAMETERS_WRAP" value="1"/>
    <option name="EXTENDS_LIST_WRAP" value="1"/>
    <option name="THROWS_KEYWORD_WRAP" value="1"/>
    <option name="METHOD_CALL_CHAIN_WRAP" value="1"/>
    <option name="BINARY_OPERATION_WRAP" value="1"/>
    <option name="BINARY_OPERATION_SIGN_ON_NEXT_LINE" value="true"/>
    <option name="TERNARY_OPERATION_WRAP" value="1"/>
    <option name="TERNARY_OPERATION_SIGNS_ON_NEXT_LINE" value="true"/>
    <!-- Forces to add braces even for one-liner -->
    <!-- Completely follow Google Java Format-->
    <option name="FOR_STATEMENT_WRAP" value="1"/>
    <option name="ARRAY_INITIALIZER_WRAP" value="1"/>
    <option name="WRAP_COMMENTS" value="true"/>
    <option name="IF_BRACE_FORCE" value="3"/>
    <option name="DOWHILE_BRACE_FORCE" value="3"/>
    <option name="WHILE_BRACE_FORCE" value="3"/>
    <option name="FOR_BRACE_FORCE" value="3"/>
    <JavaCodeStyleSettings>
	    <!-- Debatable -->
	    <option name="ANNOTATION_PARAMETER_WRAP" value="0" />
	    <!-- Debatable -->
		<option name="ALIGN_MULTILINE_ANNOTATION_PARAMETERS" value="false" />
		<!-- Debatable -->
		<option name="RECORD_COMPONENTS_WRAP" value="0" />
		<!-- Debatable -->
		<option name="ALIGN_MULTILINE_RECORDS" value="false" />
		<!-- Debatable -->
		<option name="NEW_LINE_AFTER_LPAREN_IN_RECORD_HEADER" value="false" />
		<!-- Debatable -->
		<option name="RPAREN_ON_NEW_LINE_IN_RECORD_HEADER" value="false" />
		
        <!-- Disable wildcard imports -->
        <!-- Completely follow Google Java Format-->
        <option name="INSERT_INNER_CLASS_IMPORTS" value="true"/>
        <option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999"/>
        <option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999"/>
        <option name="PACKAGES_TO_USE_IMPORT_ON_DEMAND">
            <value/>
        </option>
        <!-- Specify the layout describing how imports should be organized  -->
        <!-- Completely follow Google Java Format -->
        <option name="IMPORT_LAYOUT_TABLE">
            <value>
                <package name="" withSubpackages="true" static="true"/>
                <emptyLine/>
                <package name="" withSubpackages="true" static="false"/>
            </value>
        </option>
        <!-- Specify the Javadocs format  -->
        <!-- Mostly follow Google Java Format -->
        <!-- Settings > Editor > Code Style > Java > JavaDoc -->
        <option name="JD_ALIGN_PARAM_COMMENTS" value="false"/>
        <option name="JD_ALIGN_EXCEPTION_COMMENTS" value="false"/>
        <option name="JD_P_AT_EMPTY_LINES" value="false"/>
        <option name="JD_KEEP_INVALID_TAGS" value="false"/>
        <option name="JD_KEEP_EMPTY_PARAMETER" value="false"/>
        <option name="JD_KEEP_EMPTY_EXCEPTION" value="false"/>
        <option name="JD_KEEP_EMPTY_RETURN" value="false"/>
        <option name="JD_PRESERVE_LINE_FEEDS" value="true"/>
    </JavaCodeStyleSettings>
</code_scheme>
```

## 1.2. Explanation

Note: Some other options are explained in the `XML - Java section`.

### 1.2.1. **`IMPORT_LAYOUT_TABLE`**

Before:
```java
import java.util.List;
import static java.lang.Math.max;
import java.io.File;
import static java.lang.System.out;
```

After:
```java
import static java.lang.Math.max;
import static java.lang.System.out;

import java.io.File;
import java.util.List;
```

### 1.2.2. **`KEEP_BLANK_LINES_BEFORE_RBRACE=0`**

Before:
```java
public class Student {
	public static void main(String[] args) {
	}
	// There's a lot of blank lines here...




}
```


After:
```java
public class Student {
	public static void main(String[] args) {
	}
	// There's no blank lines right now!
}
```

### 1.2.3. **`BLANK_LINES_AFTER_CLASS_HEADER=1`**

Before:
```java
public class Student {





	// There's a lot of blank lines after class header.
	public static void main(String[] args) {
	}
}
```


After:
```java
public class Student {

	// There's only one blank lines after class header right now!
	public static void main(String[] args) {
	}
}
```

### 1.2.4. **`KEEP_BLANK_LINES_IN_CODE=1`**

Before:
```java
public class Student {
	public static void main(String[] args) {
		String name = "Student";



		int age = 25;



		double cgpa = 4.0;


	}
}
```


After:
```java
public class Student {
	public static void main(String[] args) {
		String name = "Student";
		
		int age = 25;
		
		double cgpa = 4.0;
	}
}
```



# 2. XML - Java
---
```xml
<!-- Controversial options will be pointed out only -->
<!-- Others will just follow Google Java Format -->
<codeStyleSettings language="JAVA">
    <option name="KEEP_CONTROL_STATEMENT_IN_ONE_LINE" value="true"/>
    <option name="KEEP_BLANK_LINES_IN_CODE" value="1"/>
    <option name="BLANK_LINES_AFTER_CLASS_HEADER" value="1"/>
    
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_PARAMETERS" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_RESOURCES" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_BINARY_OPERATION" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_TERNARY_OPERATION" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_CHAINED_METHODS" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_GROUP_FIELD_DECLARATIONS" value="false"/>
    <!-- Debatable -->
    <option name="ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS" value="false"/>
    <!-- Debatable -->
    <option name="THROWS_LIST_WRAP" value="0" />
    <!-- Debatable -->
    <option name="ALIGN_MULTILINE_THROWS_LIST" value="false" />  
    
    <option name="ALIGN_MULTILINE_FOR" value="false"/>
    <option name="CALL_PARAMETERS_WRAP" value="1"/>
    <option name="METHOD_PARAMETERS_WRAP" value="1"/>
    <option name="EXTENDS_LIST_WRAP" value="1"/>
    <option name="THROWS_KEYWORD_WRAP" value="1"/>
    <option name="METHOD_CALL_CHAIN_WRAP" value="1"/>
    <option name="BINARY_OPERATION_WRAP" value="1"/>
    <option name="BINARY_OPERATION_SIGN_ON_NEXT_LINE" value="true"/>
    <option name="TERNARY_OPERATION_WRAP" value="1"/>
    <option name="TERNARY_OPERATION_SIGNS_ON_NEXT_LINE" value="true"/>
    <option name="SPACE_BEFORE_METHOD_CALL_PARENTHESES" value="false"/>
    <option name="FOR_STATEMENT_WRAP" value="1"/>
    <option name="ARRAY_INITIALIZER_WRAP" value="1"/>
    <option name="WRAP_COMMENTS" value="true"/>
    <option name="IF_BRACE_FORCE" value="3"/>
    <option name="DOWHILE_BRACE_FORCE" value="3"/>
    <option name="WHILE_BRACE_FORCE" value="3"/>
    <option name="FOR_BRACE_FORCE" value="3"/>
    <option name="PARENT_SETTINGS_INSTALLED" value="true"/>
    <indentOptions>
        <option name="INDENT_SIZE" value="4"/>
        <option name="CONTINUATION_INDENT_SIZE" value="4"/>
        <option name="TAB_SIZE" value="4"/>
        <!-- Debatable -->
        <option name="USE_RELATIVE_INDENTS" value="false"/>
    </indentOptions>
</codeStyleSettings>
```

# 3. Debatable
---
### 3.1. ALIGN_MULTILINE_PARAMETERS
---
```xml
<option name="ALIGN_MULTILINE_PARAMETERS" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > 

Original:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678470810.png)

`true`:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678489271.png)

`false`:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678547588.png)

In both scenario, the IDE will split the parameters automatically after they exceed the maximum length allowed.

### 3.2. ALIGN_MULTILINE_RESOURCES
---
```xml
<option name="ALIGN_MULTILINE_RESOURCES" value="false"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > ``

value=false:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678680136.png)

value=true:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678704990.png)

### 3.3. TERNARY_OPERATION_SIGNS_ON_NEXT_LINE
---
```xml
<option name="TERNARY_OPERATION_SIGNS_ON_NEXT_LINE" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Ternary operation

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679575363.png)

(Currently in use) value=1, Wrap if long
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679709672.png)

value=2, Chop down if long
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679717323.png)

value=3, Wrap always
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679717323.png)


### 3.4. USE_RELATIVE_INDENTS
---
```xml
<option name="USE_RELATIVE_INDENTS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Tabs and Indents

Will affect a lot of indentation*

false:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753687279836.png)

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753687285968.png)


true:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753687174109.png)

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753687225192.png)


### 3.5. ALIGN_MULTILINE_CHAINED_METHODS
---
```xml
<option name="ALIGN_MULTILINE_CHAINED_METHODS" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753689643497.png)

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753689727344.png)

### 3.6. ALIGN_MULTILINE_BINARY_OPERATION
---
```xml
<option name="ALIGN_MULTILINE_BINARY_OPERATION" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690017217.png)

With `relative indentation`
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690046779.png)

With `relative indentation` + `Binary Multiline Alignment` (The latter will overwrite)
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690077324.png)

### 3.7. ALIGN_MULTILINE_TERNARY_OPERATION
---
```xml
<option name="ALIGN_MULTILINE_TERNARY_OPERATION" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690428950.png)

With relative indentation
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690391299.png)

With `relative indentation` + `Binary Multiline Alignment` (The latter will overwrite)
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690402531.png)

### 3.8. Annotation
---
```xml
<!-- Debatable -->
<!-- Means the annotation will wrap parameters if they are too long -->
<option name="ANNOTATION_PARAMETER_WRAP" value="1" />

<!-- Debatable -->
<!-- Means the parameters will be aligned -->
<option name="ALIGN_MULTILINE_ANNOTATION_PARAMETERS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any config
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690841783.png)

Wrap if Long (value=1)
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690852092.png)

Align when multiline
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690872475.png)

### 3.9. ALIGN_GROUP_FIELD_DECLARATIONS
---
```xml
<option name="ALIGN_GROUP_FIELD_DECLARATIONS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Before:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690972633.png)

After:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753690999414.png)

### 3.10. ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS
---
```xml
<option name="ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Before:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691017808.png)

After:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691033889.png)


### 3.11. Throw
---
```xml
<option name="THROWS_LIST_WRAP" value="0" />
<option name="ALIGN_MULTILINE_THROWS_LIST" value="false" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691511544.png)

With Wrap if Long (value=1)
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691528958.png)

With Wrap if Long (value=1) + Align when multiline
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691553921.png)

### 3.12. Records
---
```xml
<option name="RECORD_COMPONENTS_WRAP" value="0" />  
<option name="ALIGN_MULTILINE_RECORDS" value="false" />
<option name="NEW_LINE_AFTER_LPAREN_IN_RECORD_HEADER" value="false" />  
<option name="RPAREN_ON_NEW_LINE_IN_RECORD_HEADER" value="false" />
```

Without any settings
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691859449.png)

With `RECORD_COMPONENTS_WRAP`/ Manual line breaks + `ALIGN_MULTILINE_RECORDS`

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691772851.png)

![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691788426.png)

With all options
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753691809593.png)

>[!info]
>Basically, by turning on all the options, it will automatically turn it into the last format. Without `wrap_if_long`, the developers would need to insert the line break manually.

## 2.2. Explanation (Can skip)
---
### 2.2.1. Control Statement
---
- `KEEP_CONTROL_STATEMENT_IN_ONE_LINE`
- `IF_BRACE_FORCE`
```xml
<option name="KEEP_CONTROL_STATEMENT_IN_ONE_LINE" value="false"/>
<option name="IF_BRACE_FORCE" value="3"/>
```
This configuration will automatically add braces for if-else statements and force them to be in multiline.

Before:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678097613.png)

After:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678113061.png)

### 2.2.2. KEEP_BLANK_LINES_IN_CODE
---
```xml
<option name="KEEP_BLANK_LINES_IN_CODE" value="1"/>
```
This option will keep only 1 blank line between code section.

Before:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678234660.png)

After:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678244625.png)

### 2.2.3. BLANK_LINES_AFTER_CLASS_HEADER
---
```xml
<option name="BLANK_LINES_AFTER_CLASS_HEADER" value="1"/>
```
This option will add a blank line after class header.

Before:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678308474.png)

After:
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753678316907.png)

### 2.2.4. METHOD_PARAMETERS_WRAP
---
```xml
<option name="METHOD_PARAMETERS_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Method declaration parameters

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679046117.png)

**(Currently in use) value=1, Wrap if long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679066369.png)

value=2, Chop down if long
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679100614.png)


### 2.2.5. EXTENDS_LIST_WRAP
---
```xml
<option name="EXTENDS_LIST_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Extends/implements/permits list

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679253181.png)

**(Currently in Use) value=1, Wrap if Long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679271848.png)

### 2.2.6. THROWS_KEYWORD_WRAP
---
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Throws keyword

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679324716.png)

**(Currently in Use) value=1, Wrap if long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679295281.png)

### 2.2.7. METHOD_CALL_CHAIN_WRAP
---
```xml
<option name="METHOD_CALL_CHAIN_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Chained method calls

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679360584.png|516x31]]

**(Currently in Use) value=1, Wrap if long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679402060.png)

### 2.2.8. BINARY_OPERATION_WRAP
---
```xml
<option name="BINARY_OPERATION_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Binary expressions

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679459853.png)

**(Currently in Use) value=1, Wrap if long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679479551.png)

### 2.2.9. BINARY_OPERATION_SIGN_ON_NEXT_LINE
---
```xml
<option name="BINARY_OPERATION_SIGN_ON_NEXT_LINE" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Binary expressions > Operation sign on next line

value=false
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679500819.png)

**(Currently in Use) value=true**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679532081.png)

### 2.2.10. TERNARY_OPERATION_WRAP
---
```xml
<option name="TERNARY_OPERATION_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Ternary operation > Operation sign on next line

value=false
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679769749.png)

**(Currently in use) value=true**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679777101.png)

### 2.2.11. FOR_STATEMENT_WRAP
---
```xml
<option name="FOR_STATEMENT_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > `for()` statement

value=false, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679905229.png)

**(Currently in use) value=1, Wrap if long**
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679845378.png)

### 2.2.12. ARRAY_INITIALIZER_WRAP
---
```xml
<option name="ARRAY_INITIALIZER_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Array Initializer

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753680329024.png)

**(Currently in use) value=1, Wrap if long
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753680285340.png)

value=2, Chop down if long
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753680315957.png)

### 2.2.13. Other braces
---
```xml
<option name="DOWHILE_BRACE_FORCE" value="3"/>  
<option name="WHILE_BRACE_FORCE" value="3"/>  
<option name="FOR_BRACE_FORCE" value="3"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

value=3, (Force braces=Always, always add braces)



# 3. `.editorconfig` (Can be ignored)
---
```config
.editorconfig
[*.java]

# Basic indentation settings
indent_style = space
indent_size = 4
ij_continuation_indent_size = 4
ij_java_use_relative_indents = true
ij_java_keep_indents_on_empty_lines = true
max_line_length = 120

# To disable wildcard import (*)
ij_java_class_count_to_use_import_on_demand = unset
ij_java_names_count_to_use_import_on_demand = unset

ij_java_align_multiline_parameters = true

# Debatable.
ij_java_align_multiline_chained_methods = false

# Debatable.
ij_java_align_multiline_binary_operation = true

ij_java_align_multiline_parenthesized_expression = true  
ij_java_align_multiline_resources = true

# Debatable.
ij_java_align_multiline_ternary_operation = true

ij_java_align_multiline_text_blocks = false
ij_java_align_multiline_array_initializer_expression = false
ij_java_align_multiline_assignment = false
ij_java_align_multiline_parameters_in_calls = false  
ij_java_align_multiline_method_parentheses = false  
ij_java_align_multiline_annotation_parameters = false

# Debatable.
ij_java_align_multiline_records = true

ij_java_align_types_in_multi_catch = false  
ij_java_align_throws_keyword = true  
ij_java_align_group_field_declarations = false

# Method chain and builder methods
# ij_java_method_call_chain_wrap = normal
# ij_java_keep_builder_methods_indents = true
```
- Settings > Editor > Code Style > Java