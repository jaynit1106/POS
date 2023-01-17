<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:date="http://exslt.org/dates-and-times"
                extension-element-prefixes="date">
  <xsl:import href="fop/functions/date/date.date.xsl"/>
  <xsl:output method="xml" indent="yes"/>
  <xsl:decimal-format name="euro" decimal-separator="," grouping-separator="."/>
  <xsl:template match="/invoice-record">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm" margin-top="2cm"
                               margin-bottom="2cm" margin-left="2cm" margin-right="2cm"
                               font-family="Calibri">
          <fo:region-body/>
          <fo:region-after region-name="invoice-footer" extent="5mm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="A4">
        <fo:flow flow-name="xsl-region-body" font-size="8pt">
          <fo:block space-after="1cm"></fo:block>
          <fo:block space-before="2cm" space-after="2cm" text-align="center" font-size="20pt" font-weight="bold">
            INCREFF
          </fo:block>

          <fo:block space-after="1cm"></fo:block>
          <fo:table>
            <fo:table-column column-width="3cm"/>
            <fo:table-column column-width="6cm"/>
            <fo:table-column column-width="2cm"/>
            <fo:table-column column-width="3cm"/>
            <fo:table-column column-width="2cm"/>
            <fo:table-header font-weight="bold">
              <fo:table-row border-bottom="1px solid #eee">
                <fo:table-cell text-align="left" padding-bottom="2px">
                  <fo:block>ID</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px">
                  <fo:block>Barcode</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px">
                  <fo:block>Quantity</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px">
                  <fo:block>Price</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px">
                  <fo:block>Total</fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-header>
            <fo:table-body>
              <xsl:for-each select="line-item-records/line-item-record">
                <fo:table-row border-bottom="1px solid #eee">
                  <fo:table-cell text-align="left" padding-bottom="2px" padding-top="2px">
                    <fo:block>
                      <xsl:value-of select="id"/>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                    <fo:block>
                      <xsl:value-of select="barcode"/>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                    <fo:block>
                      <xsl:value-of select="quantity"/>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                    <fo:block>
                      <xsl:value-of select="price"/>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                    <fo:block><xsl:value-of select="total"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
              </xsl:for-each>
            </fo:table-body>
          </fo:table>
          <fo:block space-after="1cm"></fo:block>
          <fo:table>
            <fo:table-column column-width="9cm"/>
            <fo:table-column column-width="4cm"/>
            <fo:table-column column-width="4cm"/>
            <fo:table-body>
              <fo:table-row>
                <fo:table-cell>
                  <fo:block></fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                  <fo:block font-weight="bold">Subtotaal</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                  <fo:block>&#x20ac; <xsl:value-of select="format-number(invoice-total, '###.###,00', 'euro')"/>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell>
                  <fo:block></fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px" border-bottom="1px solid #eee">
                  <fo:block font-weight="bold">Totaal BTW</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px" border-bottom="1px solid #eee">
                  <fo:block>&#x20ac; <xsl:value-of select="format-number(invoice-total-vat, '###.###,00', 'euro')"/></fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell>
                  <fo:block></fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                  <fo:block font-weight="bold">Totaal incl. BTW</fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="right" padding-bottom="2px" padding-top="2px">
                  <fo:block>&#x20ac; <xsl:value-of select="format-number(invoice-total-incl-vat, '###.###,00', 'euro')"/></fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>