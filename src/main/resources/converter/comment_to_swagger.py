import re

# version: 1.0 build:failed
# convert line with private filed has comment to new line as follows:
# SAMPLE:
#     //组织id
#     private Long organizationId;
#
#     private Long organizationId;//组织id
# Converted:
#
#     @ApiModelProperty(value = "组织id")
#     private Long organizationId;

# TODO: connect to database or search from liquibase

with open("test.java", "r", encoding='utf-8') as f:
    all_lines = f.readlines()

with open("test.java", "w", encoding='utf-8') as file:
    for line_read in all_lines:
        if re.search('private', line_read):
            if line_read.find("//") != -1:
                comment = line_read[line_read.find("//") + 2:line_read.find("\n")]
                line_read = line_read[:line_read.find("/")] + "\n"
                line = "    @ApiModelProperty(value = \"" + comment + "\")\n" + line_read
                file.write(line)
            else:
                file.write(line_read)
        elif line_read.find("//") != -1:
            comment = line_read[line_read.find("//") + 2:line_read.find("\n")]
            line = "    @ApiModelProperty(value = \"" + comment + "\")\n"
            file.write(line)
        else:
            file.write(line_read)
