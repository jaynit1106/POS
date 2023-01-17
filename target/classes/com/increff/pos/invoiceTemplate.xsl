<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="date">
	<xsl:import href="fop/functions/date/date.date.xsl" />
	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="euro"
		decimal-separator="," grouping-separator="." />
	<xsl:template match="/invoice-record">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm"
					font-family="Calibri">
					<fo:region-body />
					<fo:region-after region-name="invoice-footer"
						extent="5mm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body" font-size="8pt">
					<fo:block space-after="1cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="17cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="center">
									<fo:block>
										<fo:external-graphic src="https://www.increff.com/wp-content/themes/increff/new-mega-menu/images/logo-new.png" height="20mm" content-height="scale-to-fit"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-after="1cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="4cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block font-weight="bold">From</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>Increff</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>2nd floor, Enzyme Tech Park,</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>Sector 6, HSR Layout,</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>Bengaluru, Karnataka 560102</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-after="1.5cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="1.5cm" />
						<fo:table-column column-width="2cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block font-weight="bold">Order Id: </fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>
										<xsl:value-of select="orderId" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:table>
						<fo:table-column column-width="1cm" />
						<fo:table-column column-width="7cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block font-weight="bold">Date: </fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>
										<xsl:value-of select="dates" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:table>
						<fo:table-column column-width="1cm" />
						<fo:table-column column-width="7cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block font-weight="bold">Time: </fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px">
									<fo:block>
										<xsl:value-of select="times" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-after="1cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="1cm" />
						<fo:table-column column-width="3cm" />
						<fo:table-column column-width="4cm" />
						<fo:table-column column-width="2cm" />
						<fo:table-column column-width="3cm" />
						<fo:table-column column-width="4cm" />
						<fo:table-header font-weight="bold">
							<fo:table-row border-bottom="1px solid #eee">
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>S.No.</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>Name</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>Barcode</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>Quantity</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>Price</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px">
									<fo:block>Total</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:for-each
								select="line-item-records/line-item-record">
								<fo:table-row border-bottom="1px solid #eee">
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="id" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="name" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="barcode" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="quantity" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="price" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="left"
										padding-bottom="2px" padding-top="2px">
										<fo:block>
											<xsl:value-of select="total" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
					<fo:block space-after="1cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="1cm" />
						<fo:table-column column-width="3cm" />
						<fo:table-column column-width="4cm" />
						<fo:table-column column-width="2cm" />
						<fo:table-column column-width="3cm" />
						<fo:table-column column-width="4cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block font-weight="bold">Cost</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block>
										<xsl:value-of select="cost" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block font-weight="bold">GST</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block>
										<xsl:value-of select="gst" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block font-weight="bold">Total Cost</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="left"
									padding-bottom="2px" padding-top="2px"
									border-bottom="1px solid #eee">
									<fo:block>
										<xsl:value-of select="totalCost" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

						</fo:table-body>
					</fo:table>
					<fo:block space-after="3cm"></fo:block>
					<fo:table>
						<fo:table-column column-width="17cm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="center"
									padding-bottom="2px" padding-top="2px">
									<fo:block font-weight="bold" font-size="20pt">Have A Good Day !!!</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>