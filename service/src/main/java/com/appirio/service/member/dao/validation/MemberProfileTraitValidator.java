package com.appirio.service.member.dao.validation;

import com.appirio.service.member.api.MemberProfileBasicInfo;
import com.appirio.service.member.api.MemberProfileCommunities;
import com.appirio.service.member.api.MemberProfileEducation;
import com.appirio.service.member.api.MemberProfileSkill;
import com.appirio.service.member.api.MemberProfileTrait;
import com.appirio.service.member.api.MemberProfileWork;
import com.appirio.service.member.api.MemberProfileLanguage;
import com.appirio.service.member.api.MemberProfileHobby;
import com.appirio.service.member.api.MemberProfileOrganization;
import com.appirio.service.member.api.MemberProfileDevice;
import com.appirio.service.member.api.MemberProfileSoftware;
import com.appirio.service.member.api.MemberProfileServiceProvider;
import com.appirio.service.member.api.MemberProfileSubscription;
import com.appirio.service.member.api.MemberProfilePersonalization;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the MemberProfileTraitValidator used to validate the user profile trait data
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 *
 * Version 1.1:
 * - Added more trait type validations:
 *      - LANGUAGES_TRAIT_TYPE
 *      - HOBBY_TRAIT_TYPE
 *      - ORGANIZATION_TRAIT_TYPE
 *
 * Version 1.2:
 * - Added more trait type validations:
 *      - DEVICE_TRAIT_TYPE
 *      - SOFTWARE_TRAIT_TYPE
 *      - SERVICE_PROVIDER_TRAIT_TYPE
 *      - SUBSCRIPTION_TRAIT_TYPE
 *
 * Version 1.3:
 * - Added more trait type validations:
 *      - PERSONALIZATION_TRAIT_TYPE
 *
 * @author TCCoder - thomaskranitsas
 * @version 1.3
 *
 */
public class MemberProfileTraitValidator implements CustomValidator<MemberProfileTrait> {

    /**
     * The BASIC_INFO_TRAIT_TYPE field
     */
    public static final String BASIC_INFO_TRAIT_TYPE = "basic_info";

    /**
     * The EDUCATION_TRAIT_TYPE field
     */
    public static final String EDUCATION_TRAIT_TYPE = "education";

    /**
     * The SKILL_TRAIT_TYPE field
     */
    public static final String SKILL_TRAIT_TYPE = "skill";

    /**
     * The WORK_TRAIT_TYPE field
     */
    public static final String WORK_TRAIT_TYPE = "work";

    /**
     * The COMMUNITIES_TRAIT_TYPE field
     */
    public static final String COMMUNITIES_TRAIT_TYPE = "communities";

    /**
     * The LANGUAGES_TRAIT_TYPE field
     */
    public static final String LANGUAGES_TRAIT_TYPE = "languages";

    /**
     * The HOBBY_TRAIT_TYPE field
     */
    public static final String HOBBY_TRAIT_TYPE = "hobby";

    /**
     * The ORGANIZATION_TRAIT_TYPE field
     */
    public static final String ORGANIZATION_TRAIT_TYPE = "organization";

    /**
     * The DEVICE_TRAIT_TYPE field
     */
    public static final String DEVICE_TRAIT_TYPE = "device";

    /**
     * The SOFTWARE_TRAIT_TYPE field
     */
    public static final String SOFTWARE_TRAIT_TYPE = "software";

    /**
     * The SERVICE_PROVIDER_TRAIT_TYPE field
     */
    public static final String SERVICE_PROVIDER_TRAIT_TYPE = "service_provider";

    /**
     * The SUBSCRIPTION_TRAIT_TYPE field
     */
    public static final String SUBSCRIPTION_TRAIT_TYPE = "subscription";

    /**
     * The PERSONALIZATION_TRAIT_TYPE field
     */
    public static final String PERSONALIZATION_TRAIT_TYPE = "personalization";

    /**
     * The GENDERS field
     */
    private static Set<String> GENDERS = new HashSet<String>(Arrays.asList("male", "female", "other"));

    /**
     * The TSHIRT_SIZE field
     */
    private static Set<String> TSHIRT_SIZE = new HashSet<String>(Arrays.asList("XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL"));

    /**
     * The COUNTRIES field
     */
    private static Set<String> COUNTRIES = new HashSet<String>(Arrays.asList("Afghanistan","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antarctica",
    "Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin",
    "Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Bouvet Island","Brazil","British Indian Ocean Territory","Brunei Darussalam","Bulgaria","Burkina Faso",
    "Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands","Colombia",
    "Comoros","Congo","Congo, the Democratic Republic of the","Cook Islands","Costa Rica","Cote D'Ivoire","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica",
    "Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Falkland Islands (Malvinas)","Faroe Islands","Fiji","Finland","France",
    "French Guiana","French Polynesia","French Southern Territories","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam",
    "Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Heard Island and Mcdonald Islands","Holy See (Vatican City State)","Honduras","Hong Kong","Hungary","Iceland","India",
    "Indonesia","Iran, Islamic Republic of","Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","North Korea","South Korea","Kuwait","Kyrgyzstan",
    "Lao People's Democratic Republic","Latvia","Lebanon","Lesotho","Liberia","Libyan Arab Jamahiriya","Liechtenstein","Lithuania","Luxembourg","Macao","Macedonia, the Former Yugoslav Republic of",
    "Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia, Federated States of","Moldova, Republic of",
    "Monaco","Mongolia","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island",
    "Northern Mariana Islands","Norway","Oman","Pakistan","Palau","Palestinian Territory, Occupied","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn","Poland","Portugal",
    "Puerto Rico","Qatar","Reunion","Romania","Russian Federation","Rwanda","Saint Helena","Saint Kitts and Nevis","Saint Lucia","Saint Pierre and Miquelon","Saint Vincent and the Grenadines",
    "Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa",
    "South Georgia and the South Sandwich Islands","Spain","Sri Lanka","Sudan","Suriname","Svalbard and Jan Mayen","Swaziland","Sweden","Switzerland","Syrian Arab Republic","Taiwan","Tajikistan",
    "Tanzania, United Republic of","Thailand","Timor-Leste","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine",
    "United Arab Emirates","United Kingdom","United States of America","United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu","Venezuela","Viet Nam","Virgin Islands, British",
    "Virgin Islands, U.S.","Wallis and Futuna","Western Sahara","Yemen","Zambia","Zimbabwe","Åland Islands","Bonaire, Sint Eustatius and Saba","Curaçao","Guernsey","Isle of Man","Jersey","Montenegro",
    "Saint Barthélemy","Saint Martin (French part)","Serbia","Sint Maarten (Dutch part)","South Sudan","Kosovo"));

    /**
     * The EDUCATION_TYPE field
     */
    private static Set<String> EDUCATION_TYPE = new HashSet<String>(Arrays.asList("Secondary School", "University"));

    /**
     * The LANGUAGE_LEVEL field
     */
    private static Set<String> LANGUAGE_LEVEL = new HashSet<String>(Arrays.asList("Basic", "Intermediate", "Advanced", "Native"));

    /**
     * The DEVICE_TYPES field
     */
    private static Set<String> DEVICE_TYPES = new HashSet<String>(Arrays.asList("Desktop", "Laptop", "Tablet", "Smartphone", "Console"));

    /**
     * The SOFTWARE_TYPES field
     */
    private static Set<String> SOFTWARE_TYPES = new HashSet<String>(Arrays.asList("Developer Tools", "Browser", "Productivity", "Graphics & Design", "Utilities"));

    /**
     * The SERVICE_PROVIDER_TYPES field
     */
    private static Set<String> SERVICE_PROVIDER_TYPES = new HashSet<String>(Arrays.asList("Internet Service Provider", "Mobile Carrier"));

    /**
     * Message result
     */
    private List<String> result = new ArrayList<String>();

    /**
     * Create MemberProfileTraitValidator
     *
     */
    public MemberProfileTraitValidator() {
    }

    /**
     * Validate the member profile trait
     *
     * @param memberProfileTrait the memberProfileTrait to use
     * @throws SupplyException if any error occurs
     */
    public List<String> validate(MemberProfileTrait memberProfileTrait) throws SupplyException {
        if (MemberProfileTraitValidator.BASIC_INFO_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() != 1) {
                result.add("The basic info data should be provided");
                return result;
            }
            MemberProfileBasicInfo item = (MemberProfileBasicInfo) memberProfileTrait.getTraits().getData().get(0);
            if (item.getFirstName() == null || item.getFirstName().trim().length() == 0) {
                result.add("The first name should be provided");
            }
            if (item.getLastName() == null || item.getLastName().trim().length() == 0) {
                result.add("The last name should be provided");
            }
            if (!GENDERS.contains(item.getGender())) {
                result.add("The gender should be provided as male, female or other");
            }
            if (!TSHIRT_SIZE.contains(item.getTshirtSize())) {
                result.add("The t shirt size should be provided as XXS, XS, S, M, L, XL, XXL or XXXL");
            }
            if (item.getCountry() == null) {
                result.add("The country should be provided");
            }
            if (!COUNTRIES.contains(item.getCountry())) {
                result.add("The country is invalid: '" + item.getCountry() + "'");
            }
            if (item.getBirthDate() == null) {
                result.add("The birth date should be provided");
            }
            if (item.getAddress() == null || item.getAddress().trim().length() == 0) {
                result.add("The address should be provided");
            }
            if (item.getState() == null || item.getState().trim().length() == 0) {
                result.add("The state should be provided");
            }
            if (item.getCity() == null || item.getCity().trim().length() == 0) {
                result.add("The city should be provided");
            }
            if (item.getCurrentLocation() == null || item.getCurrentLocation().trim().length() == 0) {
                result.add("The current location should be provided");
            }
            if (item.getZipCode() == null || item.getZipCode().trim().length() == 0) {
                result.add("The zip code should be provided");
            }
        } else if (MemberProfileTraitValidator.EDUCATION_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The education data should be provided");
                return result;
            }
            for (MemberProfileEducation item : (List<MemberProfileEducation>) memberProfileTrait.getTraits().getData()) {
                if (!EDUCATION_TYPE.contains(item.getType())) {
                    result.add("The education type should be provided");
                }
                if (item.getGraduated() == null) {
                    result.add("The education graduated data should be provided");
                }
                if (item.getMajor() == null) {
                    result.add("The education major should be provided");
                }
                if (item.getSchoolCollegeName() == null) {
                    result.add("The education school college name should be provided");
                }
                if (item.getTimePeriodFrom() == null) {
                    result.add("The education time period from should be provided");
                }
                if (item.getTimePeriodTo() == null) {
                    result.add("The education time period to should be provided");
                }
            }

        } else if (MemberProfileTraitValidator.WORK_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The work data should be provided");
                return result;
            }
            for (MemberProfileWork item : (List<MemberProfileWork>) memberProfileTrait.getTraits().getData()) {
                if (item.getCityTown() == null || item.getCityTown().trim().length() == 0) {
                    result.add("The work city town should be provided");
                }
                if (item.getCompany() == null || item.getCompany().trim().length() == 0) {
                    result.add("The work company should be provided");
                }
                if (item.getPosition() == null || item.getPosition().trim().length() == 0) {
                    result.add("The work position should be provided");
                }
                if (item.getTimePeriodFrom() == null) {
                    result.add("The work time period from should be provided");
                }
                if (item.getTimePeriodTo() == null) {
                    result.add("The work time period to should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.SKILL_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() != 1) {
                result.add("The skill data should be provided");
                return result;
            }
            MemberProfileSkill item = (MemberProfileSkill) memberProfileTrait.getTraits().getData().get(0);
            if (item.getDataScience() != null && item.getDataScience().contains(null)) {
                result.add("The skill data science should not contain null");
            }
            if (item.getDesign() != null && item.getDesign().contains(null)) {
                result.add("The skill design should not contain null");
            }
            if (item.getDevelopment() != null && item.getDevelopment().contains(null)) {
                result.add("The skill devlopment should not contain null");
            }

        } else if (MemberProfileTraitValidator.COMMUNITIES_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() != 1) {
                result.add("The skill data should be provided");
                return result;
            }
            MemberProfileCommunities item = (MemberProfileCommunities) memberProfileTrait.getTraits().getData().get(0);
            if (item.getBlockchain() == null) {
                result.add("The communites block chain should be provided");
            }
            if (item.getCognitive() == null) {
                result.add("The communites cognitive should be provided");
            }
            if (item.getIos() == null) {
                result.add("The communites ios should be provided");
            }
            if (item.getPredix() == null) {
                result.add("The communites prefix should be provided");
            }
        } else if (MemberProfileTraitValidator.LANGUAGES_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The language data should be provided");
                return result;
            }
            for (MemberProfileLanguage item : (List<MemberProfileLanguage>) memberProfileTrait.getTraits().getData()) {
                if (item.getLanguage() == null || item.getLanguage().trim().length() == 0) {
                    result.add("The language should be provided");
                }
                if (item.getSpokenLevel() == null || item.getSpokenLevel().trim().length() == 0) {
                    result.add("The spoken level should be provided");
                } else if (!LANGUAGE_LEVEL.contains(item.getSpokenLevel())) {
                    result.add("The spoken level should be one of: Basic, Intermediate, Advanced, Native");
                }
                if (item.getWrittenLevel() == null || item.getWrittenLevel().trim().length() == 0) {
                    result.add("The written level should be provided");
                } else if (!LANGUAGE_LEVEL.contains(item.getWrittenLevel())) {
                    result.add("The written level should be one of: Basic, Intermediate, Advanced, Native");
                }
            }
        } else if (MemberProfileTraitValidator.HOBBY_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The hobby data should be provided");
                return result;
            }
            for (MemberProfileHobby item : (List<MemberProfileHobby>) memberProfileTrait.getTraits().getData()) {
                if (item.getHobby() == null || item.getHobby().trim().length() == 0) {
                    result.add("The hobby should be provided");
                }
                if (item.getDescription() == null || item.getDescription().trim().length() == 0) {
                    result.add("The description should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.ORGANIZATION_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The organization data should be provided");
                return result;
            }
            for (MemberProfileOrganization item : (List<MemberProfileOrganization>) memberProfileTrait.getTraits().getData()) {
                if (item.getName() == null || item.getName().trim().length() == 0) {
                    result.add("The organization name should be provided");
                }
                if (item.getSector() == null || item.getSector().trim().length() == 0) {
                    result.add("The organization sector should be provided");
                }
                if (item.getCity() == null || item.getCity().trim().length() == 0) {
                    result.add("The organization city should be provided");
                }
                if (item.getTimePeriodFrom() == null) {
                    result.add("The organization time period from should be provided");
                }
                if (item.getTimePeriodTo() == null) {
                    result.add("The organization time period to should be provided");
                }
            }

        } else if (MemberProfileTraitValidator.DEVICE_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The device data should be provided");
                return result;
            }
            for (MemberProfileDevice item : (List<MemberProfileDevice>) memberProfileTrait.getTraits().getData()) {
                if (item.getDeviceType() == null || item.getDeviceType().trim().length() == 0) {
                    result.add("The device type should be provided");
                } else if (!DEVICE_TYPES.contains(item.getDeviceType())) {
                    result.add("The device type should be one of: Desktop, Laptop, Tablet, Smartphone, Console");
                }
                if (item.getManufacturer() == null || item.getManufacturer().trim().length() == 0) {
                    result.add("The manufacturer should be provided");
                }
                if (item.getModel() == null || item.getModel().trim().length() == 0) {
                    result.add("The model should be provided");
                }
                if (item.getOperatingSystem() == null || item.getOperatingSystem().trim().length() == 0) {
                    result.add("The operating system should be provided");
                }
                if (item.getOsVersion() == null || item.getOsVersion().trim().length() == 0) {
                    result.add("The OS version should be provided");
                }
                if (item.getOsLanguage() == null || item.getOsLanguage().trim().length() == 0) {
                    result.add("The OS language should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.SOFTWARE_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The software data should be provided");
                return result;
            }
            for (MemberProfileSoftware item : (List<MemberProfileSoftware>) memberProfileTrait.getTraits().getData()) {
                if (item.getSoftwareType() == null || item.getSoftwareType().trim().length() == 0) {
                    result.add("The software type should be provided");
                } else if (!SOFTWARE_TYPES.contains(item.getSoftwareType())) {
                    result.add("The software type should be one of: Developer Tools, Browser, Productivity, Graphics & Design, Utilities");
                }
                if (item.getName() == null || item.getName().trim().length() == 0) {
                    result.add("The name should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.SERVICE_PROVIDER_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The service provider data should be provided");
                return result;
            }
            for (MemberProfileServiceProvider item : (List<MemberProfileServiceProvider>) memberProfileTrait.getTraits().getData()) {
                if (item.getServiceProviderType() == null || item.getServiceProviderType().trim().length() == 0) {
                    result.add("The service provider type should be provided");
                } else if (!SERVICE_PROVIDER_TYPES.contains(item.getServiceProviderType())) {
                    result.add("The software type should be one of: Internet Service Provider, Mobile Carrier");
                }
                if (item.getName() == null || item.getName().trim().length() == 0) {
                    result.add("The name should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.SUBSCRIPTION_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The subscription data should be provided");
                return result;
            }
            for (MemberProfileSubscription item : (List<MemberProfileSubscription>) memberProfileTrait.getTraits().getData()) {
                if (item.getName() == null || item.getName().trim().length() == 0) {
                    result.add("The name should be provided");
                }
            }
        } else if (MemberProfileTraitValidator.PERSONALIZATION_TRAIT_TYPE.equals(memberProfileTrait.getTraitId())) {
            if (memberProfileTrait.getTraits().getData() == null || memberProfileTrait.getTraits().getData().size() == 0) {
                result.add("The personalization data should be provided");
                return result;
            }
            for (MemberProfilePersonalization item : (List<MemberProfilePersonalization>) memberProfileTrait.getTraits().getData()) {
                if (item.getUserConsent() == null) {
                    result.add("The user consent should be provided");
                }
            }
        } else {
            result.add("The trait id is not supported: " + memberProfileTrait.getTraitId());
        }

        return result;
    }
}
