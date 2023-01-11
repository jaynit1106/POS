<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:attribute-set name="tableBorder">
		<xsl:attribute name="border">solid 0.2mm black</xsl:attribute>
	</xsl:attribute-set>
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
					<fo:block font-size="16pt" font-weight="bold" space-after="5mm" font-family="Arial">
						Organisation Name :
						<xsl:value-of select="orgname" />
					</fo:block>
					<fo:block font-size="10pt">
						<fo:table table-layout="fixed" width="100%" border-collapse="separate">
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-header>
								<fo:table-cell xsl:use-attribute-sets="tableBorder" ><fo:block font-weight="bold">barcode</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="tableBorder" ><fo:block font-weight="bold">quantity</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="tableBorder" ><fo:block font-weight="bold">price</fo:block></fo:table-cell>
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
			<fo:table-cell xsl:use-attribute-sets="tableBorder" >
				<fo:block>
					<xsl:value-of select="barcode"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell xsl:use-attribute-sets="tableBorder" >
				<fo:block>
					<xsl:value-of select="quantity"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell xsl:use-attribute-sets="tableBorder" >
				<fo:block>
					<xsl:value-of select="price"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>