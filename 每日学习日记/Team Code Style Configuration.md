---
updated: 2025-07-27T19:23:09.168+08:00
edited_seconds: 326
---
**Useful setup**
1. Settings > Tools > Actions on Save > Reformat Code, Optimize imports, Rearrange Code, Code Cleanup
2. Reformat File Dialog (Ctrl + Alt + Shift + L) > Reformat Code, Optimize imports, Rearrange Code, Code Cleanup
3. Settings > Appearance & Behavior > System Settings > Autosave > Save files if the IDE is idle for 60 seconds
4. Settings > Editor > General > Soft Wraps > Soft Wraps these files 
5. Settings > Languages & Frameworks > Markdown > Markdown Extensions (`PlantUML`) (If you're using `PlantUML`)

**Suggestions**
1. Use `google-java-format` plugin. Refer the official page for the setup: https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config.
2. Use the following IntelliJ-based solutions:
	1. `.editorconfig`: indentation, alignment, and chain wrapping
	2. `.idea/inspectionProfiles/Project_Default .xml`: For warning code smells, security issues, and potential bugs.
	3. `.idea/codeStyles/Project.xml`: To configure Java code rearranging properties.
3. Use `SonarCube IDE` plugin. `SonarCube` can be used for detecting code smells, security issues, and potential bugs. Advanced features (such as toggling on/off for the whole team) will require advanced setup.

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

`.idea/inspectionProfiles/Project_Default .xml
```xml
<component name="InspectionProjectProfileManager">
  <profile version="1.0">
    <option name="myName" value="Project Default" />
    <inspection_tool class="SerializableHasSerialVersionUIDField" enabled="true" level="WARNING" enabled_by_default="true" />
    <inspection_tool class="MagicNumber" enabled="true" level="WARNING" enabled_by_default="true" />
    <inspection_tool class="ControlFlowStatementWithoutBraces" enabled="true" level="WARNING" enabled_by_default="true" />
  </profile>
</component>

`.idea/codeStyles/Project.xml`
<component name="ProjectCodeStyleConfiguration">
  <code_scheme name="Project" version="173">
    <editorconfig>
      <option name="ENABLED" value="false" />
    </editorconfig>
    <codeStyleSettings language="JAVA">
      <option name="SPACE_WITHIN_ARRAY_INITIALIZER_BRACES" value="true" />
      <arrangement>
        <rules>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PUBLIC>true</PUBLIC>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PROTECTED>true</PROTECTED>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PACKAGE_PRIVATE>true</PACKAGE_PRIVATE>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PRIVATE>true</PRIVATE>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PUBLIC>true</PUBLIC>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PROTECTED>true</PROTECTED>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PACKAGE_PRIVATE>true</PACKAGE_PRIVATE>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PRIVATE>true</PRIVATE>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <INITIALIZER_BLOCK>true</INITIALIZER_BLOCK>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PUBLIC>true</PUBLIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PROTECTED>true</PROTECTED>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PACKAGE_PRIVATE>true</PACKAGE_PRIVATE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <NAME>.*Service$</NAME>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <NAME>.*Validator$</NAME>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <NAME>.*Dao$</NAME>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <NAME>.*Repository$</NAME>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <NAME>.*Mapper$</NAME>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <FINAL>true</FINAL>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PUBLIC>true</PUBLIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PROTECTED>true</PROTECTED>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PACKAGE_PRIVATE>true</PACKAGE_PRIVATE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <FIELD>true</FIELD>
                  <PRIVATE>true</PRIVATE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <FIELD>true</FIELD>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <INITIALIZER_BLOCK>true</INITIALIZER_BLOCK>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <CONSTRUCTOR>true</CONSTRUCTOR>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <METHOD>true</METHOD>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <METHOD>true</METHOD>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <ENUM>true</ENUM>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <INTERFACE>true</INTERFACE>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <CLASS>true</CLASS>
                  <STATIC>true</STATIC>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <CLASS>true</CLASS>
              </match>
            </rule>
          </section>
        </rules>
      </arrangement>
    </codeStyleSettings>
  </code_scheme>
</component>
```


