---
updated: 2025-07-29T12:45:06.498+08:00
edited_seconds: 6251
---
# 1. Setup
---
If this is your first time setting up the code style configuration, kindly follow the steps below.

## 1.1. Useful options

The following options are recommended to enable automatic code formatting, rearranging, and import optimization — saving time and improving the development experience:

1. **(Recommended)**
   Go to: Use the Reformat File Dialog (Ctrl + Alt + Shift + L)
   Enable: Reformat Code, Optimize imports, Rearrange Code, Code Cleanup

2. *(Optional)* 
   Settings > Appearance & Behavior > System Settings > Autosave
   Enable: Save files if the IDE is idle for 60 seconds

3. *(Optional)*
   Settings > Editor > General > Soft Wraps
   Enable: Soft Wraps these files (It is helpful if you're using `Markdown`)

4. *(Optional)*
   Settings > Languages & Frameworks > Markdown
   Enable: Markdown Extensions (`PlantUML`) (If you're using `PlantUML`)

5. *(Optional)*
   Install the `SonarQube IDE` plugin to detect code smells, security issues, maintainability problems, and potential bugs

## 1.2. Code Style Setup

Two configuration files are provided:

| File                                      | Usage                                                                                     |
| ----------------------------------------- | ----------------------------------------------------------------------------------------- |
| `iFast-Illuminator-CodeStyle.xml`         | Defines formatting and arrangement rules (indentation, alignment, wrapping, etc.)         |
| `iFast-Illuminator-InspectionProfile.xml` | Defines inspections for code smells, potential bugs, maintainability, and security issues |

### 1.2.1. `iFastIlluminatorStyle.xml`

1. Open **File > Settings**
   ![](code-style-guide-1753754341053.png)

2. Open **Editor > Code Style > Java**
   ![](code-style-guide-1753754409931.png)


3. Click the Scheme dropdown (gear icon) > **Import Scheme > IntelliJ IDEA code style XML**
   ![](code-style-guide-1753754474305.png)

4. Select `iFast-Illuminator-CodeStyle.xml`. The scheme will load completely — the file’s location won't affect future behavior.
   ![](code-style-guide-1753763380222.png)

5. Name the scheme (or keep the default: `iFast-Illuminator`).
   ![](code-style-guide-1753761550200.png)

6. You should see the message:
   “IntelliJ IDEA code style XML settings were imported to `<Your-Scheme-Name>`”
   The scheme is now applied.
   ![](code-style-guide-1753761591355.png)

### 1.2.2. `iFast-Illuminator-InspectionProfile.xml`


1. Open **File > Settings**
   ![](code-style-guide-1753756922134.png)

2. Open **Editor > Inspection**
   ![](code-style-guide-1753756945518.png)

3. Click the Profile dropdown (gear icon) > **Import Profile**
   ![](code-style-guide-1753756965183.png)

4. Select `iFast-Illuminator-InspectionProfile.xml`
   ![](code-style-guide-1753761781298.png)

5. You should see the profile named `iFast-Illuminator`.
   Confirm that the "Java" and "JVM languages" categories are highlighted in blue — this means the configuration is applied.
   ![](code-style-guide-1753758479873.png)


6. Click **OK**.

7. Go to `.idea/inspectionProfiles/profiles_settings.xml` to verify the profile was applied:
   ![](code-style-guide-1753758571838.png)

8. For further verification, in **Editor > Inspections > Profile**, click **Copy to Project**
   ![](code-style-guide-1753758704214.png)

9. Keep the same name. This will copy the profile settings from IDE to the current project.
   ![](code-style-guide-1753758738075.png)

10. This will generate `.idea/inspectionProfiles/iFast_Illuminator.xml`. You can see the current configuration applied.
   ![](code-style-guide-1753758768863.png)

11. If using your own custom profile (e.g., `iFast-Illuminator`), you will see `profiles_settings.xml` — unless you are using the `Project Default`/ delete the `Project Default`, in which case it won't appear.
    ![](code-style-guide-1753764105137.png)

## 1.3. Edit the configuration

It is recommended to maintain these configuration files in a **centralized Git repository**. This way, when updates are made, developers can `git pull` to get the latest changes and import them effortlessly. Version control also enables rollback and collaborative improvements (via PRs/issues).

### 1.3.1. `iFast-Illuminator-CodeStyle.xml`

There are two approaches:

1. **(Less preferable yet easier)**
	1. Edit using IntelliJ, export to overwrite the previous scheme. 
	2. ❗Be cautious — this WILL overwrite useful comments and default-value options that were purposefully included. (FYI, the IDE won't show default-value options in the exported schema).
	   ![](code-style-guide-1753762145629.png)


2. **(Preferable yet manual)**
	1. Export changes as a new file, then manually merge the changes into the team version (e.g., `iFast-Illuminator-CodeStyle.xml`). 
	2. ✅ Preserves structure, comments, and intent behind the configuration.


REMINDER:
ALWAYS click **Apply** or **OK** to apply the changes before exporting — otherwise, they won’t be included in the saved schema.
- **Apply**: Saves changes and keeps the settings window open
- **OK**: Saves changes and closes the settings window

![](code-style-guide-1753762236179.png)
   
### 1.3.2. `iFast-Illuminator-InspectionProfile.xml`


This file is easier to manage. You can directly export and overwrite the old profile without losing important settings — since inspection tools are defined by class names (`class="..."`), the structure is self-explanatory (Unless you have added any comments).

![](code-style-guide-1753762610493.png)

## 1.4. FAQ

**How do I use an updated profile that someone else modified?**

You’ll need to re-import the updated `.xml` schema. Importing is required each time changes are made by others.


**Can I use `.editorconfig` and `.xml` files together?**

Yes. IntelliJ uses configuration in this order:
1. `.editorconfig` (highest precedence)
2. XML configuration (e.g., `CodeStyle.xml`)
3. IDE defaults (lowest precedence)

However, we currently do **not** use `.editorconfig`.

**Will autoformatting affect all files in the project?**

No. IntelliJ IDEA applies reformatting only to:

- Files you’re editing
- Files saved, if Actions on Save is enabled
- Files explicitly selected during a bulk reformatting action

**Why don’t my changes appear?**

Checklist:
- Make sure you clicked **Apply** or **OK** before exiting settings.
- If someone else updated the profile/schema, re-import the `.xml` file.
- If everything seems correct but still doesn’t apply, restart the IDE: Go to **File > Invalidate Caches > Invalidate and Restart**.
  ![](Team%20Code%20Style%20Configuration-1753693840322.png)
  ![](code-style-guide-1753762907879.png)


# 2. (Optional) XML
---
You can found the complete files in the folder. This section is mainly used to describe some of the fields.

## 2.1. **`IMPORT_LAYOUT_TABLE`**
---
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

## 2.2. **`KEEP_BLANK_LINES_BEFORE_RBRACE=0`**
---
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

## 2.3. **`BLANK_LINES_AFTER_CLASS_HEADER=1`**
---
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

## 2.4. **`KEEP_BLANK_LINES_IN_CODE=1`**
---
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

## 2.5. ALIGN_MULTILINE_PARAMETERS
---
```xml
<option name="ALIGN_MULTILINE_PARAMETERS" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > 

Original:
![](Team%20Code%20Style%20Configuration-1753678470810.png)

`true`:
![](Team%20Code%20Style%20Configuration-1753678489271.png)

`false`:
![](Team%20Code%20Style%20Configuration-1753678547588.png)

In both scenario, the IDE will split the parameters automatically after they exceed the maximum length allowed.

## 2.6. ALIGN_MULTILINE_RESOURCES
---
```xml
<option name="ALIGN_MULTILINE_RESOURCES" value="false"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > ``

value=false:
![](Team%20Code%20Style%20Configuration-1753678680136.png)

value=true:
![](Team%20Code%20Style%20Configuration-1753678704990.png)

## 2.7. TERNARY_OPERATION_SIGNS_ON_NEXT_LINE
---
```xml
<option name="TERNARY_OPERATION_SIGNS_ON_NEXT_LINE" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Ternary operation

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679575363.png)

(Currently in use) value=1, Wrap if long
![](Team%20Code%20Style%20Configuration-1753679709672.png)

value=2, Chop down if long
![](Team%20Code%20Style%20Configuration-1753679717323.png)

value=3, Wrap always
![](Team%20Code%20Style%20Configuration-1753679717323.png)


## 2.8. USE_RELATIVE_INDENTS
---
```xml
<option name="USE_RELATIVE_INDENTS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Tabs and Indents

Will affect a lot of indentation*

false:
![](Team%20Code%20Style%20Configuration-1753687279836.png)

![](Team%20Code%20Style%20Configuration-1753687285968.png)


true:
![](Team%20Code%20Style%20Configuration-1753687174109.png)

![](Team%20Code%20Style%20Configuration-1753687225192.png)


## 2.9. ALIGN_MULTILINE_CHAINED_METHODS
---
```xml
<option name="ALIGN_MULTILINE_CHAINED_METHODS" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

![](Team%20Code%20Style%20Configuration-1753689643497.png)

![](Team%20Code%20Style%20Configuration-1753689727344.png)

## 2.10. ALIGN_MULTILINE_BINARY_OPERATION
---
```xml
<option name="ALIGN_MULTILINE_BINARY_OPERATION" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](Team%20Code%20Style%20Configuration-1753690017217.png)

With `relative indentation`
![](Team%20Code%20Style%20Configuration-1753690046779.png)

With `relative indentation` + `Binary Multiline Alignment` (The latter will overwrite)
![](Team%20Code%20Style%20Configuration-1753690077324.png)

## 2.11. ALIGN_MULTILINE_TERNARY_OPERATION
---
```xml
<option name="ALIGN_MULTILINE_TERNARY_OPERATION" value="true" />
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](Team%20Code%20Style%20Configuration-1753690428950.png)

With relative indentation
![](Team%20Code%20Style%20Configuration-1753690391299.png)

With `relative indentation` + `Binary Multiline Alignment` (The latter will overwrite)
![](Team%20Code%20Style%20Configuration-1753690402531.png)

## 2.12. Annotation
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
![](Team%20Code%20Style%20Configuration-1753690841783.png)

Wrap if Long (value=1)
![](Team%20Code%20Style%20Configuration-1753690852092.png)

Align when multiline
![](Team%20Code%20Style%20Configuration-1753690872475.png)

## 2.13. ALIGN_GROUP_FIELD_DECLARATIONS
---
```xml
<option name="ALIGN_GROUP_FIELD_DECLARATIONS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Before:
![](Team%20Code%20Style%20Configuration-1753690972633.png)

After:
![](Team%20Code%20Style%20Configuration-1753690999414.png)

## 2.14. ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS
---
```xml
<option name="ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS" value="true" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Before:
![](Team%20Code%20Style%20Configuration-1753691017808.png)

After:
![](Team%20Code%20Style%20Configuration-1753691033889.png)


## 2.15. Throw
---
```xml
<option name="THROWS_LIST_WRAP" value="0" />
<option name="ALIGN_MULTILINE_THROWS_LIST" value="false" />
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

Without any settings
![](Team%20Code%20Style%20Configuration-1753691511544.png)

With Wrap if Long (value=1)
![](Team%20Code%20Style%20Configuration-1753691528958.png)

With Wrap if Long (value=1) + Align when multiline
![](Team%20Code%20Style%20Configuration-1753691553921.png)

## 2.16. Records
---
```xml
<option name="RECORD_COMPONENTS_WRAP" value="0" />  
<option name="ALIGN_MULTILINE_RECORDS" value="false" />
<option name="NEW_LINE_AFTER_LPAREN_IN_RECORD_HEADER" value="false" />  
<option name="RPAREN_ON_NEW_LINE_IN_RECORD_HEADER" value="false" />
```

Without any settings
![](Team%20Code%20Style%20Configuration-1753691859449.png)

With `RECORD_COMPONENTS_WRAP`/ Manual line breaks + `ALIGN_MULTILINE_RECORDS`

![](Team%20Code%20Style%20Configuration-1753691772851.png)

![](Team%20Code%20Style%20Configuration-1753691788426.png)

With all options
![](Team%20Code%20Style%20Configuration-1753691809593.png)

>[!info]
>Basically, by turning on all the options, it will automatically turn it into the last format. Without `wrap_if_long`, the developers would need to insert the line break manually.

## 2.17. Control Statement
---
- `KEEP_CONTROL_STATEMENT_IN_ONE_LINE`
- `IF_BRACE_FORCE`
```xml
<option name="KEEP_CONTROL_STATEMENT_IN_ONE_LINE" value="false"/>
<option name="IF_BRACE_FORCE" value="3"/>
```
This configuration will automatically add braces for if-else statements and force them to be in multiline.

Before:
![](Team%20Code%20Style%20Configuration-1753678097613.png)

After:
![](Team%20Code%20Style%20Configuration-1753678113061.png)

## 2.18. KEEP_BLANK_LINES_IN_CODE
---
```xml
<option name="KEEP_BLANK_LINES_IN_CODE" value="1"/>
```
This option will keep only 1 blank line between code section.

Before:
![](Team%20Code%20Style%20Configuration-1753678234660.png)

After:
![](Team%20Code%20Style%20Configuration-1753678244625.png)

## 2.19. BLANK_LINES_AFTER_CLASS_HEADER
---
```xml
<option name="BLANK_LINES_AFTER_CLASS_HEADER" value="1"/>
```
This option will add a blank line after class header.

Before:
![](Team%20Code%20Style%20Configuration-1753678308474.png)

After:
![](Team%20Code%20Style%20Configuration-1753678316907.png)

## 2.20. METHOD_PARAMETERS_WRAP
---
```xml
<option name="METHOD_PARAMETERS_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Method declaration parameters

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679046117.png)

**(Currently in use) value=1, Wrap if long**
![](Team%20Code%20Style%20Configuration-1753679066369.png)

value=2, Chop down if long
![](Team%20Code%20Style%20Configuration-1753679100614.png)


## 2.21. EXTENDS_LIST_WRAP
---
```xml
<option name="EXTENDS_LIST_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Extends/implements/permits list

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679253181.png)

**(Currently in Use) value=1, Wrap if Long**
![](Team%20Code%20Style%20Configuration-1753679271848.png)

## 2.22. THROWS_KEYWORD_WRAP
---
```xml 
<option name="THROWS_KEYWORD_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Throws keyword

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679324716.png)

**(Currently in Use) value=1, Wrap if long**
![](Team%20Code%20Style%20Configuration-1753679295281.png)

## 2.23. METHOD_CALL_CHAIN_WRAP
---
```xml
<option name="METHOD_CALL_CHAIN_WRAP" value="1"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Chained method calls

value=0, Do not wrap
![](./imgs-code_style_guide/Team%20Code%20Style%20Configuration-1753679360584.png|516x31]]

**(Currently in Use) value=1, Wrap if long**
![](Team%20Code%20Style%20Configuration-1753679402060.png)

## 2.24. BINARY_OPERATION_WRAP
---
```xml
<option name="BINARY_OPERATION_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Binary expressions

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679459853.png)

**(Currently in Use) value=1, Wrap if long**
![](Team%20Code%20Style%20Configuration-1753679479551.png)

## 2.25. BINARY_OPERATION_SIGN_ON_NEXT_LINE
---
```xml
<option name="BINARY_OPERATION_SIGN_ON_NEXT_LINE" value="true"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Binary expressions > Operation sign on next line

value=false
![](Team%20Code%20Style%20Configuration-1753679500819.png)

**(Currently in Use) value=true**
![](Team%20Code%20Style%20Configuration-1753679532081.png)

## 2.26. TERNARY_OPERATION_WRAP
---
```xml
<option name="TERNARY_OPERATION_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Ternary operation > Operation sign on next line

value=false
![](Team%20Code%20Style%20Configuration-1753679769749.png)

**(Currently in use) value=true**
![](Team%20Code%20Style%20Configuration-1753679777101.png)

## 2.27. FOR_STATEMENT_WRAP
---
```xml
<option name="FOR_STATEMENT_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > `for()` statement

value=false, Do not wrap
![](Team%20Code%20Style%20Configuration-1753679905229.png)

**(Currently in use) value=1, Wrap if long**
![](Team%20Code%20Style%20Configuration-1753679845378.png)

## 2.28. ARRAY_INITIALIZER_WRAP
---
```xml
<option name="ARRAY_INITIALIZER_WRAP" value="1"/>
```
**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces > Array Initializer

value=0, Do not wrap
![](Team%20Code%20Style%20Configuration-1753680329024.png)

**(Currently in use) value=1, Wrap if long
![](Team%20Code%20Style%20Configuration-1753680285340.png)

value=2, Chop down if long
![](Team%20Code%20Style%20Configuration-1753680315957.png)

## 2.29. Other braces
---
```xml
<option name="DOWHILE_BRACE_FORCE" value="3"/>  
<option name="WHILE_BRACE_FORCE" value="3"/>  
<option name="FOR_BRACE_FORCE" value="3"/>
```

**Manual Config**: Settings > Editor > Code Style > Java > Wrapping and Braces

value=3, (Force braces=Always, always add braces)


# 3. References
---
If you are using the `google-java-format` plugin, kindly refer to [this guide](https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config). However, some configurations (e.g., indentation) provided by the Google Java Style may not align with the habits or preferences of the majority.