import csv

def generate_sparql_script(csv_file):
    script = ""

    with open(csv_file, 'r') as file:
        reader = csv.DictReader(file, delimiter=';')
        
        for row in reader:
            resource_schema = row['resource_schema']
            block_type = row['block_type']
            amount = row['amount']

            axiom = f"{{\n  :{resource_schema} rdf:type owl:Class ;\n"
            axiom += f"             rdfs:subClassOf [ rdf:type owl:Restriction ;\n"
            axiom += f"                               owl:onProperty :hasBlockType ;\n"
            axiom += f"                               owl:minCardinality {amount} ;\n"
            axiom += f"                               owl:onClass :{block_type}\n"
            axiom += f"                             ] .\n}}\n"
            script += axiom

    return script

# Example usage:
csv_file_path = 'block_types_min.csv'
sparql_script = generate_sparql_script(csv_file_path)

print(sparql_script)