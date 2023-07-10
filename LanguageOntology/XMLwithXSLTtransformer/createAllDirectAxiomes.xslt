<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="UTF-8" />
    <!-- define a key for grouping each relation by its sourceConcept and the relationship type -->
    <xsl:key name="sourceTypeKey" match="relation" use="concat(sourceConcept, '|', type)" />

    <xsl:template match="directRelations">
        <axiomes>
            <xsl:for-each
                select="relation[generate-id() = generate-id(key('sourceTypeKey', concat(sourceConcept, '|', type))[1])]">
                <xsl:variable name="sourceConcept" select="sourceConcept" />
        <xsl:variable
                    name="type" select="type" />
                <!-- foreach unique releation for each targetConcept, create an axiome, seperated by
                the logical or -->
        <axiome>
                    <sourceConcept>
                        <xsl:value-of select="$sourceConcept" />
                    </sourceConcept>
                    <type>
                        <xsl:value-of select="$type" />
                    </type>
                    <cardinality>only</cardinality>
                    <targetConcepts>
                        <xsl:for-each
                            select="key('sourceTypeKey', concat(sourceConcept, '|', type))">
                            <xsl:if test="position() = 1">(</xsl:if>
              <xsl:value-of
                                select="targetConcept" />
              <xsl:if test="position() != last()"> or </xsl:if>
              <xsl:if
                                test="position() = last()">)</xsl:if>
                        </xsl:for-each>
                    </targetConcepts>
                </axiome>
            </xsl:for-each>
        </axiomes>
    </xsl:template>
</xsl:stylesheet>