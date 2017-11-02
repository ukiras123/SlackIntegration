package com.kiran.service.utilities;

/**
 * @author Kiran
 * @since 10/21/17
 */
public enum ApiDetail {

    ProductAPI("product",
            "8180",
            "/Users/kgautam/Drive/projects/slate/product-api",
            "fathom test product_item/product_create.rb -c env:local id:1 brand:BQ types:CHOICE_BUNDLE",
            "fathom test product_item -c env:local id:1 brand:BQ"),
    FulfillmentAPI("fulfillment",
            "8190",
            "/Users/kgautam/Drive/projects/slate/fulfillment-api",
            "fathom test fulfillment_item/fulfillment_create.rb -c env:local id:1 brand:BQ types:SINGLE",
            "fathom test fulfillment_item -c env:local id:1 brand:BQ types:SINGLE"),
    PriceListAPI("price",
            "8200",
            "/Users/kgautam/Drive/projects/slate/price-list-api",
            "fathom test price_list/price_create.rb -c env:local id:1 brand:BQ",
            "fathom test price_list -c env:local id:1 brand:BQ"),
    ScheduleAPI("schedule",
            "8210",
            "/Users/kgautam/Drive/projects/slate/schedule-api",
            "fathom test schedule/schedule_create.rb -c env:local id:1 brand:BQ",
            "fathom test schedule -c env:local id:1 brand:BQ"),
    ContinuityAPI("continuity",
            "8220",
            "/Users/kgautam/Drive/projects/slate/continuity-api",
            "fathom test continuity/continuity_create.rb -c env:local id:1 brand:BQ",
            "fathom test continuity -c env:local id:1 brand:BQ"),
    InstallmentAPI("installment",
            "8230",
            "/Users/kgautam/Drive/projects/slate/installment-api",
            "fathom test installment/installment_create.rb -c env:local id:1 brand:BQ",
            "fathom test installment -c env:local id:1 brand:BQ"),
    MarketingAPI("marketing",
            "8240",
            "/Users/kgautam/Drive/projects/slate/marketing-api",
            "fathom test marketing_program/marketing_program_create.rb -c env:local id:1 brand:BQ",
            "fathom test marketing_program -c env:local id:1 brand:BQ"),
    PriceModelAPI("price_model",
            "8250",
            "/Users/kgautam/Drive/projects/slate/price-model-api",
            "fathom test price_model/price_model_create.rb -c env:local id:1 brand:BQ",
            "fathom test price_model -c env:local id:1 brand:BQ types:SINGLE"),
    TieredShippingAPI("tiered_shipping",
            "8260",
            "/Users/kgautam/Drive/projects/slate/tiered-shipping-rates-api",
            "fathom test tiered_shipping/tiered_shipping_create.rb -c env:local id:1 brand:BQ",
            "fathom test tiered_shipping -c env:local id:1 brand:BQ"),
    ProductShippingAPI("product_shipping",
            "8270",
            "/Users/kgautam/Drive/projects/slate/product-shipping-rates-api",
            "fathom test product_shipping/product_shipping_create.rb -c env:local id:1 brand:BQ",
            "fathom test product_shipping -c env:local id:1 brand:BQ"),
    ValueSetAPI("value_set",
            "8280",
            "/Users/kgautam/Drive/projects/slate/value-set-api",
            "fathom test value_set -c env:local id:1 brand:BQ",
            "fathom test value_set -c env:local id:1 brand:BQ"),
    Approved("approved",
            "8180",
            "/Users/kgautam/Drive/projects/slate/product-api",
            "fathom test fixed_bundle/bundle_approved_databse.rb -c env:local brand:BQ",
            "fathom test fixed_bundle/bundle_approved_databse.rb -c env:local brand:BQ");

    private final String api;
    private final String port;
    private final String repoLocation;
    private final String smokeTestFathomCommand;
    private final String regressionFathomCommand;


    ApiDetail(String api, String port, String repoLocation, String smokeTestFathomCommand, String regressionFathomCommand) {
        this.api = api;
        this.port = port;
        this.repoLocation = repoLocation;
        this.smokeTestFathomCommand = smokeTestFathomCommand;
        this.regressionFathomCommand = regressionFathomCommand;
    }


    public String getApi() {
        return api;
    }

    public String getPort() {
        return port;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public String getSmokeTestFathomCommand() {
        return smokeTestFathomCommand;
    }

    public String getRegressionFathomCommand() {
        return regressionFathomCommand;
    }

    public static ApiDetail fromString(String api) {
        for (ApiDetail b : ApiDetail.values()) {
            if (b.api.equalsIgnoreCase(api)) {
                return b;
            }
        }
        return null;
    }
}


