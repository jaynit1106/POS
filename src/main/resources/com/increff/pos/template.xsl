<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="organization">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-left="1cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family="Arial" font-size="7pt"
						font-weight="normal">
						Organisation Name :
						<xsl:value-of select="orgname" />
					</fo:block>
					<fo:block font-size="10pt">
						<fo:table>
							<fo:table-column column-width="3cm" />
							<fo:table-column column-width="2cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-header>
								<fo:table-cell><fo:block>brand</fo:block></fo:table-cell>
								<fo:table-cell><fo:block>category</fo:block></fo:table-cell>
								<fo:table-cell><fo:block>revenue</fo:block></fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="branch"/>
							</fo:table-body>
						</fo:table>
					</fo:block>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="branch">
		<fo:table-row>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="brand"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="category"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="revenue"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>