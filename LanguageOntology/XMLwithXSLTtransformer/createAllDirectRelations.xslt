<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" />
    <!-- relationships as top level -->
    <xsl:template match="relationships">
        <!-- create every single direct relationship -->
        <directRelations>
            <xsl:apply-templates select="source/target" />
        </directRelations>
    </xsl:template>

    <!-- for every given target set variable names -->
    <xsl:template match="target">
        <xsl:variable name="sourceConcept" select="../@concept" />
        <xsl:variable name="targetConcept" select="@concept" />
        <xsl:variable name="relations" select="@relations" />

        <!-- relations are grouped together, therefore split them -->
        <xsl:call-template name="splitRelations">
            <xsl:with-param name="sourceConcept" select="$sourceConcept" />
            <xsl:with-param name="targetConcept" select="$targetConcept" />
            <xsl:with-param name="relations" select="$relations" />
        </xsl:call-template>
    </xsl:template>

    <!-- template for splitting the grouped relationships into characters -->
    <xsl:template name="splitRelations">
        <xsl:param name="sourceConcept" />
        <xsl:param name="targetConcept" />
        <xsl:param name="relations" />
        <xsl:param name="index" select="1" />

        <xsl:variable name="relation" select="substring($relations, $index, 1)" />

        <!-- test each character and set the name of the relation -->
        <xsl:if test="$relation != ''">
            <xsl:choose>
                <!-- AccessRelationship -->
                <xsl:when test="$relation = 'a'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>accesses</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- CompositionRelationship -->
                <xsl:when test="$relation = 'c'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>composedOf</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- FlowRelationship -->
                <xsl:when test="$relation = 'f'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>flowsTo</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- AggregationRelationship -->
                <xsl:when test="$relation = 'g'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>aggregates</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- AssignmentRelationship -->
                <xsl:when test="$relation = 'i'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>assignedTo</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- InfluenceRelationship -->
                <xsl:when test="$relation = 'n'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>influence</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- AssociationRelationship -->
                <xsl:when test="$relation = 'o'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>associatedWith</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- RealizationRelationship -->
                <xsl:when test="$relation = 'r'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>realizes</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- SpecializationRelationship -->
                <xsl:when test="$relation = 's'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>specializes</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- TriggeringRelationship -->
                <xsl:when test="$relation = 't'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>triggers</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- ServingRelationship -->
                <xsl:when test="$relation = 'v'">
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>usedBy</type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:when>

                <!-- Otherwise set the character -->
                <xsl:otherwise>
                    <relation>
                        <sourceConcept>
                            <xsl:value-of select="$sourceConcept" />
                        </sourceConcept>
                        <type>
                            <xsl:value-of select="$relation" />
                        </type>
                        <targetConcept>
                            <xsl:value-of select="$targetConcept" />
                        </targetConcept>
                    </relation>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:call-template name="splitRelations">
                <xsl:with-param name="sourceConcept" select="$sourceConcept" />
                <xsl:with-param name="targetConcept" select="$targetConcept" />
                <xsl:with-param name="relations" select="$relations" />
                <xsl:with-param name="index" select="$index + 1" />
            </xsl:call-template>
        </xsl:if>

    </xsl:template>
</xsl:stylesheet>