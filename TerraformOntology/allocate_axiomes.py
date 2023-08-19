import xml.etree.ElementTree as ET

def append_file(filename, list):
    with open(filename, 'a') as file:
        for string in list[:-1]:
            file.write(f"{string}$")
        file.write(f"{list[-1]}\n")

def get_first_nested_level(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()

    resource_schemas = [child for child in root.findall("*")]

    return resource_schemas

def get_all_block_types(resource_schema):
    block_types = [child for child in resource_schema.findall("./block/block_types/*")]
    for block_type in block_types:
        for block_type_element in block_type.findall("*"):
            if block_type_element.tag != "block" and block_type_element.tag != "nesting_mode":
                required = False
                
                append_file('allocate_block_types.csv', [resource_schema.tag, block_type.tag,block_type_element.tag,block_type_element.text])
            else:
               append_file('allocate_block_types.csv', [resource_schema.tag, block_type.tag,"empty","empty"])


def get_all_block_types_attributes(resource_schema):
    block_types = [child for child in resource_schema.findall("./block/block_types/*")]
    for block_type in block_types:
        for block_type_element in block_type.findall("*"):
            if block_type_element.tag == "block":
                for attribute in block_type_element.findall("./attributes/*"):
                    for attribute_ele in attribute.findall("*"):
                        append_file('allocate_block_types_attributes.csv',[resource_schema.tag,block_type.tag,attribute.tag,attribute_ele.tag,attribute_ele.text])


def get_all_attributes(resource_schema):
    attributes = [child for child in resource_schema.findall("./block/attributes/*")]
    for attribute in attributes:
        for attribute_element in attribute.findall("*"):
            append_file('allocate_attributes.csv', [resource_schema.tag,attribute.tag,attribute_element.tag,attribute_element.text])
            #if attribute_element.tag == "required":
            #    append_file('allocate_attributes.csv', [resource_schema.tag, attribute.tag,"required"])
            #else:
            #    append_file('allocate_attributes.csv', [resource_schema.tag, attribute.tag,"optional"])


def get_all_datatypes(resource_schema):
    datatypes = [child for child in resource_schema.findall("*")]
    for datatype in datatypes:
        if datatype.tag != 'block':
            append_file('allocate_datatypes.csv', resource_schema.tag, datatype.tag)

# Usage
xml_file = 'azurermResourceSchemaNoDesKindNoDefi.xml'
all_resource_schemas = get_first_nested_level(xml_file)


for resource_schema in all_resource_schemas:
    #get_all_block_types(resource_schema)
    get_all_block_types_attributes(resource_schema)
    #get_all_attributes(resource_schema)