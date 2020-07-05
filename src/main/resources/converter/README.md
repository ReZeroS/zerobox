# autowork-tool

This project supposed to make your work easily by using some tools such like automantic work scripts or common utils.

## Converter

### 1. Swagger annotation converter
    
```py
# version: 1.0 build:failed
# convert line with private filed has comment to new line as follows:
# SAMPLE:
#     //预算组织id
#     private Long organizationId;
#
#     private Long organizationId;//预算组织id
# Converted:
#
#     @ApiModelProperty(value = "预算组织id")
#     private Long organizationId;

# TODO: connect to database or search from liquibase
```

[Here the Code](./comment_to_swagger.py)

### 2. Sql column list

```
select listagg(column_name,', ') within group(order by column_name) csv
  from ( select column_name from user_tab_columns where table_name = 'SYS_SUPPLIER');

```

If u do not wanna to type every column manaualy then u can use the above code and get string like fieldA, fieldB, ...
