package com.capitalbanker.cgb.payments.externaltransfer.model.services;


import com.capitalbanker.cgb.common.commissions.model.objects.GlobalCommissionBO;
import com.capitalbanker.cgb.common.commissions.model.objects.responses.SaveCommissionListResponse;
import com.capitalbanker.cgb.common.commissions.model.services.CommissionsSessionEJBBeanBean;
import com.capitalbanker.cgb.common.commissions.model.services.CommissionsSessionEJBBeanLocal;
import com.capitalbanker.cgb.common.commissions.model.structs.CommissionStruct;
import com.capitalbanker.cgb.common.commissions.model.structs.CommissionStructUtil;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.AdviceLanguageBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.ClientCommonBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.CountryBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.CurrencyBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.DetailedClientBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.GetBankUserActivityListResponse;
import com.capitalbanker.cgb.common.gnrc.model.objects.businessobjects.RibautBeneficiaryDetailsBO;
import com.capitalbanker.cgb.common.gnrc.model.objects.responses.AccountDetailsResponse;
import com.capitalbanker.cgb.common.gnrc.model.objects.responses.GetRibInfoResponse;
import com.capitalbanker.cgb.common.gnrc.model.responses.GetPassbookFlagResponse;
import com.capitalbanker.cgb.common.gnrc.model.services.CommonGenericCommonModelSessionEJBLocal;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.businessobjects.EconomicCodeBO;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.businessobjects.PaymentReportingCodeBO;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.businessobjects.ResidenceTypeCodeBO;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.contexts.PaymentReportingContextBO;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.responses.GetPaymentReportingDefaultValuesResponse;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.responses.GetPaymentReportingTypeResponse;
import com.capitalbanker.cgb.common.paymentreporting.model.objects.responses.ValidatePaymentReportingValuesResponse;
import com.capitalbanker.cgb.common.paymentreporting.model.services.CommonPaymentReportingCommonModelSessionEJBLocal;
import com.capitalbanker.cgb.common.utils.StringUtils;
import com.capitalbanker.cgb.payments.exchangeticket.common.model.objects.businessObjects.ExchangeReferenceBO;
import com.capitalbanker.cgb.payments.exchangeticket.common.model.objects.responses.GetExchangeAmountResponse;
import com.capitalbanker.cgb.payments.exchangeticket.common.model.objects.responses.GetExchangeAuthorizedLimitResponse;
import com.capitalbanker.cgb.payments.exchangeticket.common.model.services.PaymentsExchangeTicketCommonModelSessionEJBBeanLocal;
import com.capitalbanker.cgb.payments.externaltransfer.model.comparators.CurrencyComparator;
import com.capitalbanker.cgb.payments.externaltransfer.model.constants.PaymentsExternalTransferModelConstants;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.Account;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.Applic;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.BankCode;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.BankCounter;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.BankCounterPK;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.BankState;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.ClearingTransfer;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.ClearingTransferPeriodicity;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.Client;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.Currency;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.LegacyUser;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.PaymentReportCode;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.PaymentReportCrpCode;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.SepaTransfer;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.SepaTransferPeriodicity;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.SwiftAbbreviation;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.SwiftTransfer;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.TransferPeriodicity;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.TransferType;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.TransferValidity;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.User;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.UserAuthorizedApplic;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.responses.CheckTransferTypeEnabledResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.responses.GetExternalTransferTypeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.entities.responses.SaveClearingTransferResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.enums.ExternalTransferValidationEnum;
import com.capitalbanker.cgb.payments.externaltransfer.model.enums.KeySwift;
import com.capitalbanker.cgb.payments.externaltransfer.model.enums.TransferTypeEnum;
import com.capitalbanker.cgb.payments.externaltransfer.model.enums.VirsepaStateEnum;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.ChequeAttributesBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.CodeDescriptionBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.ExternalTransferBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.TransferBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.AccountCorrespFXYFilteredBicBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.BicBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.ClearingTransferBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.Correspondent;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.RTGSTransferTypeBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SepaMotiveBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SepaTransferBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SepaTransferCancellationBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftAdviceLanguage;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftBDLZone;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftCharge;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftTransferBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftTransferMode;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.businessobjects.SwiftTransferPeriodicity;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.comparators.ExternalTransferComparator;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.ClearingTransferContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.ClearingTransferSummaryContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.ExternalTransferContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.SepaTransfeSummaryContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.SepaTransferContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.SwiftTransferContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.contexts.SwiftTransferSummaryContextBO;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.enums.ExternalTransferComparatorType;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.predicates.SwiftTransferModeRTGSPredicate;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.AccountBankBenefResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CalculateIbanFromRibKeyResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CalculateRibKeyResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CalculateSirenCodeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ChaficPostInsertResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ChaficPreInsertUpdateResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckExchangeAmountIsValidResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckExchangeTicketInThresholdResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIfDateIsOnWeekendResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIs50fValidResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIsAmountInLedgerLimitResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIsBDLAmountInLimitResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIsTillControlledForSwiftTransferModeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckIsTillModelExistentResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckSwiftVersionResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.CheckUserAtHeadOfficeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ClearingTransferPreValidateResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ControlAMLResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ControlChequeNumberResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ControlChequeRefForModeQResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GenerateOperationReferenceResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetAccount50kFlagResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetAmountDescriptionResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetBdlZoneInformationResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetCancelSepaCancellationResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetChaficOperationNumberResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetChequeNumAndRelativeRefAccessInfoResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetCommissionDetailsResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetDefaultAdviceGenerationTypeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetExchangeRefFromOperRefResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetMaxAllowedOperDateResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetOperationAmountsResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetOperationBenefBankBicAndCountryResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetOrderingPartyDefaultAddressResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetOrderingPartyNameAndCityResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetPaymentReportDefaultValuesResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.GetReportPaymentTypeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ProcessPaidChequeResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SaveSepaTransferCancellationResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SaveSepaTransferResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SaveSwiftTransferResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SepaTransferPreValidateResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SetOperationWeightResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.SwiftTransferPreValidateResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.ValidateWeightSignatureResponse;
import com.capitalbanker.cgb.payments.externaltransfer.model.objects.responses.VerifyPaymentReportingValuesResponse;
import com.capitalbanker.cgb.payments.transfers.model.entities.BankBic;
import com.capitalbanker.cgb.payments.transfers.model.entities.Country;
import com.capitalbanker.cgb.payments.transfers.model.entities.ResidenceType;
import com.capitalbanker.cgb.payments.transfers.model.enums.TransferCallingTfEnum;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.BacsNumberBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.BankBicBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.BankCodeBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.BankCounterCodeBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.CorrespondentBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.InternetTransferBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.ReceivedSwiftTransferBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.ServiceBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.TransferAccountBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.TransferCurrencyBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.TransferModeBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.TransferPeriodicityBO;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CalculateAccountKeyResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CalculateAmountCounterValueResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CalculateBankAccountIbanResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CalculateTransferDateResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CheckAmountLedgerLimitResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CheckControlBbanBancBicResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CheckIbanValidResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.CheckValidateAccKeyWithFrIbanResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetAccountFinalProhibitionCodeResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetCancelTransferResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetChequeNumberAttributesResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetCutoffDateResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetDefaultOrderingPartyAttributesResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetDefaultValueDatesResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetOperOverdraftInfoResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetTransferStatusResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetUserDefaultTillResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetUserOperationAuthorizationResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.GetValidateTransferResponse;
import com.capitalbanker.cgb.payments.transfers.model.objects.responses.ValidateInternetTrsfCommAmntInRangeResponse;
import com.capitalbanker.cgb.payments.transfers.model.services.PaymentsTransferCommonModelSessionEJBLocal;
import com.capitalbanker.common.model.businessobjects.FxyBO;
import com.capitalbanker.common.model.businessobjects.PlsqlInputParameter;
import com.capitalbanker.common.model.businessobjects.PlsqlOutputParameter;
import com.capitalbanker.common.model.businessobjects.PlsqlParameter;
import com.capitalbanker.common.model.responses.CRUDOperationResponseImpl;
import com.capitalbanker.common.model.responses.PLSQLGlobalFunctionResponse;
import com.capitalbanker.common.model.utils.EnumUtils;
import com.capitalbanker.common.model.utils.FxyUtils;
import com.capitalbanker.common.model.utils.ModelUtils;
import com.capitalbanker.common.model.utils.QueryUtils;
import com.capitalbanker.common.model.utils.objects.EnumLovItem;

import com.google.common.collect.Collections2;

import java.lang.reflect.InvocationTargetException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.mappings.structures.ObjectRelationalDataTypeDescriptor;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.OraclePLSQLTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;

@Stateless(name = "ExternalTransferSessionEJBBean",
           mappedName = "PaymentsExternalTransferApp-PaymentsExternalTransferModel-ExternalTransferSessionEJBBean")
public class ExternalTransferSessionEJBBeanBean extends CommissionsSessionEJBBeanBean implements ExternalTransferSessionEJBBeanLocal {

    private static final Logger logger = Logger.getLogger(ExternalTransferSessionEJBBeanBean.class.getName());

    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "PaymentsExternalTransferModel")
    private EntityManager em;

    @Resource
    private UserTransaction userTransaction;

    @EJB
    PaymentsTransferCommonModelSessionEJBLocal paymentsTransferCommonModelSessionEJBLocal;

    @EJB
    CommonGenericCommonModelSessionEJBLocal commonGenericCommonModelSessionEJBLocal;

    @EJB
    CommissionsSessionEJBBeanLocal commissionSessionEJB;

    @EJB
    PaymentsExchangeTicketCommonModelSessionEJBBeanLocal paymentsExchangeTicketCommonModelSessionEJBBeanLocal;

    @EJB
    CommonPaymentReportingCommonModelSessionEJBLocal paymentReportingSessionEJB;

    private static final String PLSQL_GLOBAL_PACKAGE_NAME = "pk_globalws";
    private static final String PLSQL_GLOBAL_GENERIC_PACKAGE_NAME = "pk_globalws_generic";
    private static final String PLSQL_GLOBAL_TRANSACTION_PACKAGE_NAME = "pk_globalws_transactions";
    private static final String PLSQL_UI_UTIL_PACKAGE_NAME = "pk_capb_ui_et_util";
    private static final String PLSQL_UI_TRANSFER_PACKAGE_NAME = "pk_capb_ui_trsf_util";
    private static final String PLSQL_CAPB_CHAFIC_PACKAGE_NAME = "pk_capb_chafic";
    private static final String PLSQL_CHAFIC_PACKAGE_NAME = "pk_chafic";

    public ExternalTransferSessionEJBBeanBean() {
    }

    public List<BacsNumberBO> getBacsNumberBOList(String account, String username, String applic, String language) {
        List<BacsNumberBO> bacsnumberList = paymentsTransferCommonModelSessionEJBLocal.getBacsNumberBOList(account, username, applic, language);

        return bacsnumberList != null ? bacsnumberList :
               new ArrayList<com.capitalbanker.cgb.payments.transfers.model.objects.businessobjects.BacsNumberBO>();
    }

    private String getUpperCaseSafely(String word) {
        return !StringUtils.isEmpty(word) ? word.toUpperCase() : word;
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
    }

    //LK for test, to be removed
    public Object findEntityById(String id, Class clazz) {
        return em.find(clazz, id);
    }

    /** <code>select o from SepaTransfer o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SepaTransfer> getSepaTransferFindAll() {
        return em.createNamedQuery("SepaTransfer.findAll", SepaTransfer.class).getResultList();
    }

    /** <code>select o from ClearingTransferPeriodicity o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ClearingTransferPeriodicity> getClearingTransferPeriodicityFindAll() {
        return em.createNamedQuery("ClearingTransferPeriodicity.findAll", ClearingTransferPeriodicity.class).getResultList();
    }

    /** <code>select o from ClearingTransfer o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ClearingTransfer> getClearingTransferFindAll() {
        return em.createNamedQuery("ClearingTransfer.findAll", ClearingTransfer.class).getResultList();
    }

    /** <code>select o from TransferType o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TransferType> getTransferTypeFindAll() {
        return em.createNamedQuery("TransferType.findAll", TransferType.class).getResultList();
    }

    /** <code>select o from Country o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Country> getCountryFindAll() {
        return em.createNamedQuery("Country.findAll", Country.class).getResultList();
    }

    public List<Country> getBeneficiaryCountryList() {
        return getCountryFindAll();
    }

    /** <code>select o from SwiftTransfer o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftTransfer> getSwiftTransferFindAll() {
        return em.createNamedQuery("SwiftTransfer.findAll", SwiftTransfer.class).getResultList();
    }

    public List<SwiftTransferPeriodicity> getSwiftTransferPeriodicityFindAll() {
        String sql = "SELECT x1 CODE, y1 DESCRIPTION FROM v_xy_virest_period";

        List<SwiftTransferPeriodicity> swiftTransferPeriodicityList =
            QueryUtils.executeNativeQuery(sql, SwiftTransferPeriodicity.class, new ArrayList<Object>(), em);
        return swiftTransferPeriodicityList;
    }

    /** <code>select o from Account o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Account> getAccountFindAll() {
        return em.createNamedQuery("Account.findAll", Account.class).getResultList();
    }

    /** <code>select o from SepaTransferPeriodicity o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SepaTransferPeriodicity> getSepaTransferPeriodicityFindAll() {
        return em.createNamedQuery("SepaTransferPeriodicity.findAll", SepaTransferPeriodicity.class).getResultList();
    }


    /** <code>select o from TransferValidity o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TransferValidity> getTransferValidityFindAll() {
        return em.createNamedQuery("TransferValidity.findAll", TransferValidity.class).getResultList();
    }

    public List<EnumLovItem> getStatusList(String language) {
        return commonGenericCommonModelSessionEJBLocal.getStatusList(language, true);
    }

    public List<SwiftBDLZone> getSwiftBDLZoneFindAll() {
        String sql = "SELECT code, description FROM v_xy_virest_zonebdl";

        List<SwiftBDLZone> swiftBDLZoneList = QueryUtils.executeNativeQuery(sql, SwiftBDLZone.class, new ArrayList<Object>(), em);
        return swiftBDLZoneList;
    }

    public List<SwiftBDLZone> getSwiftBDLZoneFindAllDistinct() {
        String sql = "SELECT DISTINCT code, description FROM v_xy_virest_zonebdl";

        List<SwiftBDLZone> swiftBDLZoneList = QueryUtils.executeNativeQuery(sql, SwiftBDLZone.class, new ArrayList<Object>(), em);
        return swiftBDLZoneList;
    }

    public List<SwiftCharge> getSwiftChargeValue() {
        String sql = "SELECT code, description, isDefault FROM v_xy_virest_charg";

        List<SwiftCharge> swiftChargeList = QueryUtils.executeNativeQuery(sql, SwiftCharge.class, new ArrayList<Object>(), em);
        return swiftChargeList;
    }

    /**
     * Function to return priodicity List for Home screen LOV based on transfer type.
     * @param transferType
     * @return
     */
    public List<CodeDescriptionBO> getTransferPeriodicitySearch(String transferType) {
        List queryResult = null;
        String jpql = null;
        Class cls = null;

        if (transferType == null) {
            jpql = "TransferPeriodicity.findAll";
            cls = TransferPeriodicity.class;
        } else {
            switch (transferType.toLowerCase()) {
            case "virsepa":
                jpql = "SepaTransferPeriodicity.findAll";
                cls = SepaTransferPeriodicity.class;
                break;
            case "virchac":
                jpql = "ClearingTransferPeriodicity.findAll";
                cls = ClearingTransferPeriodicity.class;
                break;
            case "virest":
                jpql = null;
                cls = SwiftTransferPeriodicity.class;
            }
        }

        if (jpql != null)
            queryResult = em.createNamedQuery(jpql, cls).getResultList();

        if (jpql == null && cls.equals(SwiftTransferPeriodicity.class))
            queryResult = getSwiftTransferPeriodicityFindAll();

        List<CodeDescriptionBO> codeDescriptionBOList = new ArrayList<CodeDescriptionBO>();
        if (queryResult != null) {
            Object queryResultObject;
            CodeDescriptionBO codeDescBo;
            for (int i = 0; i < queryResult.size(); i++) {
                queryResultObject = queryResult.get(i);
                codeDescBo = null;

                if (queryResultObject instanceof SepaTransferPeriodicity)
                    codeDescBo = new CodeDescriptionBO((SepaTransferPeriodicity) queryResultObject);
                else if (queryResultObject instanceof SwiftTransferPeriodicity)
                    codeDescBo = new CodeDescriptionBO((SwiftTransferPeriodicity) queryResultObject);
                else if (queryResultObject instanceof ClearingTransferPeriodicity)
                    codeDescBo = new CodeDescriptionBO((ClearingTransferPeriodicity) queryResultObject);
                else if (queryResultObject instanceof TransferPeriodicity)
                    codeDescBo = new CodeDescriptionBO((TransferPeriodicity) queryResultObject);

                if (codeDescBo != null)
                    codeDescriptionBOList.add(codeDescBo);
            }
        }
        return codeDescriptionBOList;
    }

    /**
     * Returns the list transfer Modes
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of transfer Modes
     */
    public List<TransferModeBO> getModeTransferBOList(String treasuryFlag, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getModeTransferBOListByTreasury(treasuryFlag, username, applic, language);
    }

    /**
     * Returns the list Services
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of Services
     */
    public List<ServiceBO> getServiceBOList(String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getServiceBOList(username, applic, language);
    }

    public List<BankBicBO> getAdvancedSepaAndBankBicSearch(String genericFilter, String countryIsoFilter, String bicCodeFilter, String bankNameFilter,
                                                           String cityNameFilter, String keySwiftFlag, String inSepaFlag) {
        return getSepaAndBankBicSearch(genericFilter, countryIsoFilter, bicCodeFilter, bankNameFilter, cityNameFilter, keySwiftFlag, inSepaFlag, null,
                                       null);
    }

    /**
     *
     * Function to return list of bank bics from SEPAROUTE and BANKFILE (Native) for the homescreen.
     * @param genericFilter
     * @param countryIsoFilter
     * @param bicCodeFilter
     * @param bankNameFilter
     * @param cityNameFilter
     * @param keySwiftFlag
     * @param inSepaFlag
     * @return
     */
    public List<BankBicBO> getSepaAndBankBicSearch(String genericFilter, String countryIsoFilter, String bicCodeFilter, String bankNameFilter,
                                                   String cityNameFilter, String keySwiftFlag, String inSepaFlag, String bicNationFilter,
                                                   String participantTargetFilter) {
        List<BankBicBO> bankBicBOList =
            paymentsTransferCommonModelSessionEJBLocal.getTransferBankBicBOSearch(genericFilter, countryIsoFilter, bicCodeFilter, bankNameFilter,
                                                                                  cityNameFilter, keySwiftFlag, inSepaFlag, bicNationFilter,
                                                                                  participantTargetFilter);
        return bankBicBOList;
    }

    /**
     * Function to return list of externl transfers for homepage search.
     * @param clientId
     * @param operationReference
     * @param startDate
     * @param endDate
     * @param periodCode
     * @param validityCode
     * @param beneficiaryName
     * @param beneficiaryBankCountryIso
     * @param beneficiaryBankBic
     * @param transferTypeCode
     * @return
     */


    public List<ExternalTransferBO> getExternalTransferSearch(String clientId, String operationReference, Date startDate, Date endDate,
                                                              String periodCode, String validityCode, String beneficiaryName,
                                                              String beneficiaryBankCountryIso, String beneficiaryBankBic, String transferTypeCode) {


        List<ExternalTransferBO> externalTransferBOList = new ArrayList<ExternalTransferBO>();

        if (transferTypeCode == null || transferTypeCode.equalsIgnoreCase(TransferTypeEnum.Swift.toString()))
            externalTransferBOList.addAll(getSwiftTransferList(clientId, operationReference, startDate, endDate, periodCode, validityCode,
                                                               beneficiaryName, beneficiaryBankCountryIso, beneficiaryBankBic));

        if (transferTypeCode == null || transferTypeCode.equalsIgnoreCase(TransferTypeEnum.Sepa.toString()))
            externalTransferBOList.addAll(getSepaTransferList(clientId, operationReference, startDate, endDate, periodCode, validityCode,
                                                              beneficiaryName, beneficiaryBankCountryIso, beneficiaryBankBic));

        if (transferTypeCode == null || transferTypeCode.equalsIgnoreCase(TransferTypeEnum.Clearing.toString()))
            externalTransferBOList.addAll(getClearingTransferList(clientId, operationReference, startDate, endDate, periodCode, validityCode,
                                                                  beneficiaryName, beneficiaryBankCountryIso));

        Collections.sort(externalTransferBOList, new ExternalTransferComparator(ExternalTransferComparatorType.operationDate));

        return externalTransferBOList;
    }

    /** Wizard Step 1 **/

    public List<BankBic> getAdvancedBankBicSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                  String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter) {
        return getBankBicSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive, isKeySwiftFilter);

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<BankBic> getBankBicSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                          String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter) {
        return paymentsTransferCommonModelSessionEJBLocal.getBankBicSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter,
                                                                           isSepaTransferActive, isKeySwiftFilter);

    }

    public List<BankBic> getOrderingInstitutionBankBicSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                             String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter) {
        return getBankBicSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive, isKeySwiftFilter);
    }

    public List<BankBic> getBeneficiaryBankBicFilteredSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                             String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter,
                                                             String bicNationFilter) {
        List<BankBic> bankBicList =
            getBankBicNationFilteredSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive,
                                           isKeySwiftFilter, bicNationFilter);
        return bankBicList;
    }

    public List<BankBic> getOrderingPartyBankBicSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                       String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter) {
        List<BankBic> list =
            getBankBicSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive, isKeySwiftFilter);
        return list;
    }

    public List<BankBic> getCorrespBenefBankBicFilteredSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                              String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter,
                                                              String bicNationFilter) {
        return getBankBicNationFilteredSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive,
                                              isKeySwiftFilter, bicNationFilter);
    }

    public List<BankBic> getIntermediaryBankBicFilteredSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                              String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter,
                                                              String bicNationFilter) {
        return getBankBicNationFilteredSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter, cityNameFilter, isSepaTransferActive,
                                              isKeySwiftFilter, bicNationFilter);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindInfoToBeneficiary(String test) {
        return getSwiftAbbreviationSearch("72", "");
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindInfoToCorrespondantFiltered(String rtgsFilter, String username, String applic,
                                                                                       String language) {
        List<SwiftAbbreviation> list = getSwiftAbbreviationFilteredSearch("72", null, rtgsFilter, username, applic, language);
        return list;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindInfoToCorrespondant(String test) {
        return getSwiftAbbreviationSearch("72", "");
    }

    public AccountBankBenefResponse getAccountBankBenefResponse(String cptbqbe, String username, String applic, String language) {
        List<PlsqlParameter> parameters = new ArrayList<>();

        parameters.add(new PlsqlInputParameter("p_i_cptbqbe", JDBCTypes.VARCHAR_TYPE, cptbqbe));
        parameters.add(new PlsqlInputParameter("p_i_username", JDBCTypes.VARCHAR_TYPE, username));
        parameters.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        parameters.add(new PlsqlInputParameter("p_i_language", JDBCTypes.VARCHAR_TYPE, language));

        parameters.add(new PlsqlOutputParameter("p_o_ctlxbq", JDBCTypes.VARCHAR_TYPE));
        parameters.add(new PlsqlOutputParameter("p_o_nombqbe", JDBCTypes.VARCHAR_TYPE));
        parameters.add(new PlsqlOutputParameter("p_o_vilbqbe", JDBCTypes.VARCHAR_TYPE));
        parameters.add(new PlsqlOutputParameter("p_o_clebqbe", JDBCTypes.VARCHAR_TYPE));
        parameters.add(new PlsqlOutputParameter("p_o_mesbqbe", JDBCTypes.VARCHAR_TYPE));
        parameters.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        Map<String, Object> result = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "getAccountBankBenef", parameters, em);

        AccountBankBenefResponse response = new AccountBankBenefResponse();
        response.setFnReturn((String) result.get("RESULT"));
        response.setCtlxbq((String) result.get("p_o_ctlxbq"));
        response.setNombqbe((String) result.get("p_o_nombqbe"));
        response.setVilbqbe((String) result.get("p_o_vilbqbe"));
        //        response.setClebqbe((boolean) result.get("p_o_clebqbe"));
        response.setMesbqbe((String) result.get("p_o_mesbqbe"));
        response.setErrorText((String) result.get("p_o_errtxt"));

        return response;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindAccountBankBeneficiary(String test) {
        return getSwiftAbbreviationSearch("57", "");
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindInstructionCode(String abbreviation) {
        return getSwiftAbbreviationSearch("23", abbreviation);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindBankOperCode(String test) {
        return getSwiftAbbreviationSearch("23", "B/");
    }

    public List<SwiftAbbreviation> getSwiftAbbreviationSearch(String fieldFilter, String abbreviationFilter) {
        return getSwiftAbbreviationFilteredSearch(fieldFilter, abbreviationFilter, null, null, null, null);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFilteredSearch(String fieldFilter, String abbreviationFilter, String rtgsFilter,
                                                                      String username, String applic, String language) {
        //        String jpql = "SELECT o FROM SwiftAbbreviation o " + "     WHERE 1 = 1 ";



        //
        //        if (fieldFilter != null) {
        //            jpql += "   AND o.field = :field ";
        //        }
        //
        //        if (abbreviationFilter != null) {
        //            jpql += "   AND o.abbreviation LIKE CONCAT( :abbreviation , '%') ";
        //        }
        //        jpql += "   ORDER BY o.abbreviation ASC ";
        //        Query query = em.createQuery(jpql, SwiftAbbreviation.class);
        //
        //        if (abbreviationFilter != null) {
        //            query.setParameter("field", fieldFilter);
        //        }
        //        if (abbreviationFilter != null) {
        //            query.setParameter("abbreviation", abbreviationFilter);
        //        }

        String sql = "SELECT code id, champ, abbrev, explic FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getSwftabrList(?, ?, ?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(fieldFilter);
        params.add(abbreviationFilter);
        params.add(rtgsFilter);
        params.add(username);
        params.add(applic);
        params.add(language);

        return QueryUtils.executeNativeQuery(sql, SwiftAbbreviation.class, params, em);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SwiftAbbreviation> getSwiftAbbreviationFindAll() {
        List<SwiftAbbreviation> result = em.createNamedQuery("SwiftAbbreviation.findAll", SwiftAbbreviation.class).getResultList();
        return result;
    }

    /** wizard **/
    public ExternalTransferContextBO getGenerateExternalTransferContext(String clientId, String username, String language, String wizardMode,
                                                                        String operationReference, String applic, String accessContext,
                                                                        String callingTf, String warningMessage, String creditType,
                                                                        String returnToSender) {

        ExternalTransferContextBO externalTransferContextBO = new ExternalTransferContextBO();
        setCoreBankingLanguage(language);
        BankState bankState = getBankState();
        externalTransferContextBO.setBankState(bankState);
        externalTransferContextBO.setAccessContext(accessContext);

        if (language == null)
            throw new IllegalArgumentException("Invalid User language");

        if (TransferCallingTfEnum.RECEIVED_SWIFT.getCode().equalsIgnoreCase(callingTf)) {
            ReceivedSwiftTransferBO receivedSwiftTransferBO =
                getReceivedSwiftTransferBO(operationReference, creditType, returnToSender, username, applic, language);
            externalTransferContextBO.setReceivedSwiftTransferBO(receivedSwiftTransferBO);

            // set the return to sender variable in the context
            externalTransferContextBO.setReturnToSender(returnToSender);

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getBeneficiaryCountryIso()))
                externalTransferContextBO.setBeneficiaryBankCountryBO(new CountryBO(receivedSwiftTransferBO.getBeneficiaryCountryIso()));

            String beneficiaryBankBicCode = null;
            if (TransferTypeEnum.Sepa.toString().equalsIgnoreCase(applic)) {
                if (!StringUtils.isEmpty(receivedSwiftTransferBO.getSepaBeneficiaryBankBic()))
                    beneficiaryBankBicCode = receivedSwiftTransferBO.getSepaBeneficiaryBankBic(); // accinbic
            } else if (TransferTypeEnum.Swift.toString().equalsIgnoreCase(applic)) {
                if (!StringUtils.isEmpty(receivedSwiftTransferBO.getBeneficiaryBankBic()))
                    beneficiaryBankBicCode = receivedSwiftTransferBO.getBeneficiaryBankBic(); // bicbqbe
                else
                    beneficiaryBankBicCode = receivedSwiftTransferBO.getOurCorrespondantBic(); // bicncr
            }

            externalTransferContextBO.setBeneficiaryBankBicCode(beneficiaryBankBicCode);
            externalTransferContextBO.setBeneficiaryBankBic(new BankBicBO(beneficiaryBankBicCode));
        } else if (TransferCallingTfEnum.INTERNET_TRANSFER.getCode().equalsIgnoreCase(callingTf)) {
            InternetTransferBO internetTransferBO = getInternetTransferBO(operationReference, username, applic, language);
            externalTransferContextBO.setInternetTransferBO(internetTransferBO);

            // Fill step 0 fields
            if (!StringUtils.isEmpty(internetTransferBO.getBeneficiaryCountryIsoCode()))
                externalTransferContextBO.setBeneficiaryBankCountryBO(new CountryBO(internetTransferBO.getBeneficiaryCountryIsoCode()));

            if (!TransferTypeEnum.Clearing.toString().equalsIgnoreCase(applic)) {
                externalTransferContextBO.setBeneficiaryBankBicCode(internetTransferBO.getBeneficiaryBankBic());
                externalTransferContextBO.setBeneficiaryBankBic(new BankBicBO(internetTransferBO.getBeneficiaryBankBic()));
            }
        }

        if (!StringUtils.isEmpty(clientId)) {
            DetailedClientBO clientBO = new DetailedClientBO(clientId);
            externalTransferContextBO.setDebitClientBO(clientBO);
        }

        wizardMode = wizardMode == null ? PaymentsExternalTransferModelConstants.CREATE : wizardMode;
        externalTransferContextBO.setWizardMode(wizardMode);
        externalTransferContextBO.setCallingTf(callingTf);

        externalTransferContextBO.getErrorResponse().addWarningText(warningMessage);

        if (!PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode)) {
            if (operationReference == null)
                externalTransferContextBO.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF_NULL", language));
            else {
                externalTransferContextBO.setOperationReference(operationReference);
                //Get data of step 0
                GetOperationBenefBankBicAndCountryResponse resp = getOperationBenefBankBicAndCountry(operationReference, username, applic, language);
                externalTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(resp);
                if (externalTransferContextBO.hasError())
                    return externalTransferContextBO;

                if (resp.getBeneficiaryBankBic() != null)
                    externalTransferContextBO.setBeneficiaryBankBic(new BankBicBO(resp.getBeneficiaryBankBic()));
                // KJ CBKS-1990
                if (resp.getBeneficiaryCountryIsoCode() != null) {
                    CountryBO countryBO = new CountryBO(resp.getBeneficiaryCountryIsoCode());
                    countryBO.setName(resp.getBeneficiaryCountryName());
                    externalTransferContextBO.setBeneficiaryBankCountryBO(countryBO);
                }

                // fill RTGSTransferTypeLOV
                if ("VIREST".equals(applic) && externalTransferContextBO.getBeneficiaryBankCountryBO() != null &&
                    "IQ".equals(externalTransferContextBO.getBeneficiaryBankCountryBO().getIsoCode2Chars())) {
                    if (isRTGSFlagEnabled()) {
                        List<RTGSTransferTypeBO> rtgsTransferTypeBOList = getRTGSTransferModeBOList(username, applic, language);
                        String transferType = getSwiftTransferType(externalTransferContextBO.getOperationReference());
                        if (rtgsTransferTypeBOList != null && !rtgsTransferTypeBOList.isEmpty() && !StringUtils.isEmpty(transferType)) {
                            for (RTGSTransferTypeBO rtgsTransferBO : rtgsTransferTypeBOList) {
                                if (transferType.equals(rtgsTransferBO.getTransferMode())) {
                                    externalTransferContextBO.setRtgsTransferTypeBO(rtgsTransferBO);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (externalTransferContextBO.hasError())
            return externalTransferContextBO;

        User user = getUser(username);
        if (user == null)
            throw new IllegalArgumentException(translate("MSG_INVALID_BANK_USER", language));

        externalTransferContextBO.setUser(user);
        externalTransferContextBO.setUserLanguage(language);

        LegacyUser legacyUser = getLegacyUser(user.getUsercode());
        externalTransferContextBO.setLegacyUser(legacyUser);

        //Get the bank user activity list
        GetBankUserActivityListResponse bankUserAccListResp = commonGenericCommonModelSessionEJBLocal.getBankUserActivityList(legacyUser.getExpl());
        externalTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(bankUserAccListResp);
        if (!bankUserAccListResp.hasError())
            externalTransferContextBO.getLegacyUser().setActivityList(bankUserAccListResp.getActivityList());

        if (!PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode)) {
            externalTransferContextBO.setOperationDate(bankState.getCurrentDate());
            externalTransferContextBO.setCutoffDate(bankState.getCurrentDate());
        }

        CheckTransferTypeEnabledResponse checkTransferTypeEnabled = checkTransferTypeEnabled(TransferTypeEnum.Clearing.toString());

        // is Clearing Active
        if (checkTransferTypeEnabled != null)
            externalTransferContextBO.setClearingActive("Y".equals(checkTransferTypeEnabled.getTransferTypeEnabledFlag()));

        checkTransferTypeEnabled = checkTransferTypeEnabled(TransferTypeEnum.Sepa.toString());

        // is Sepa Active
        if (checkTransferTypeEnabled != null)
            externalTransferContextBO.setSepaActive("Y".equals(checkTransferTypeEnabled.getTransferTypeEnabledFlag()));

        checkTransferTypeEnabled = checkTransferTypeEnabled(TransferTypeEnum.Swift.toString());
        // is Swift Active
        if (checkTransferTypeEnabled != null)
            externalTransferContextBO.setSwiftActive("Y".equals(checkTransferTypeEnabled.getTransferTypeEnabledFlag()));

        // is RTGS Active
        externalTransferContextBO.setRtgsActive(isRTGSFlagEnabled());

        return externalTransferContextBO;
    }

    public SwiftTransferContextBO getGenerateSwiftTransferContextBO(ExternalTransferContextBO externalTransferContextBO) {
        ExternalTransferContextBO ctx = externalTransferContextBO;
        //clear all previous error messages
        if (ctx != null)
            ctx.getErrorResponse().clearAllResponses();

        SwiftTransferContextBO swiftCtxt = new SwiftTransferContextBO(externalTransferContextBO);

        if (swiftCtxt.isCallingTfReceivedSwift() && swiftCtxt.getReceivedSwiftTransferBO() != null)
            fillSwiftContextObjectFromReceivedSwift(swiftCtxt, swiftCtxt.getReceivedSwiftTransferBO());
        else if (swiftCtxt.isCallingTfInternetTransfer() && swiftCtxt.getInternetTransferBO() != null)
            fillSwiftContextObjectFromInternetPayment(swiftCtxt, swiftCtxt.getInternetTransferBO());

        SwiftTransferBO initialTrsf = null;
        if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) {
            initialTrsf =
                getSwiftTransferBO(ctx.getOperationReference(), ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                   ctx.getUserLanguage());
            if (initialTrsf == null)
                externalTransferContextBO.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF", ctx.getUserLanguage()));
            else {
                List<GlobalCommissionBO> initialComissionList =
                    getOperationCommissionList(ctx.getOperationReference(), initialTrsf.getDebitAmount(), initialTrsf.getCreditAmount(),
                                               initialTrsf.getDebitAccount(), initialTrsf.getCreditAccount(), ctx.getUser().getUsername(),
                                               TransferTypeEnum.Swift.toString(), ctx.getUserLanguage(), ctx.getWizardMode());

                fillSwiftContextObjectInit(initialTrsf, swiftCtxt, initialComissionList, false);
            }
        }

        //check if filtering on user own accounts is needed
        swiftCtxt.setFilterUserOwnAccounts(isFilterUserOwnAccounts());

        if (PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) {
            BankBicBO bankBicBO = externalTransferContextBO.getBeneficiaryBankBic();
            if (bankBicBO != null && "Y".equals(bankBicBO.getKeySwiftFlag()))
                swiftCtxt.setKeySwift("S");
        }

        //retrieve default attributes
        GetDefaultAdviceGenerationTypeResponse defaultAdviceGenTypeResp =
            getDefaultAdviceGenerationType(TransferTypeEnum.Swift.toString(), ctx.getUser().getServiceCode());
        swiftCtxt.setDefaultAdviceTypeResponse(defaultAdviceGenTypeResp);
        swiftCtxt.getErrorResponse().handlePLSQLFunctionResponse(defaultAdviceGenTypeResp);

        //retrieve default charges type
        if (PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) {
            SwiftCharge swiftCharge;
            swiftCtxt.setIbanControl(externalTransferContextBO.getIbanControl());
            if (ctx.getReceivedSwiftTransferBO() != null && "MT103".equals(ctx.getReceivedSwiftTransferBO().getMessageType())) {
                swiftCharge = getSwiftCharge(ctx.getReceivedSwiftTransferBO().getDetailsOfCharge());
                swiftCtxt.setSwiftCharge(swiftCharge);
            } else {
                String defaultCharges = getDefaultCharges();
                if (defaultCharges != null) {
                    swiftCharge = getSwiftCharge(defaultCharges);
                    swiftCtxt.setSwiftCharge(swiftCharge);
                }
            }
        }

        // IbanControl should always be checked if RTGS
        if (swiftCtxt.getRtgsTransferTypeBO() != null) {
            swiftCtxt.setIbanControl("O");

            // Set Transfer Mode if RTGS
            SwiftTransferMode selectedTransferMode = new SwiftTransferMode();
            selectedTransferMode.setCode(swiftCtxt.getRtgsTransferTypeBO().getTransferMode());
            swiftCtxt.setTransferMode(selectedTransferMode);
        }

        //Control RibautBeneficiaryDetails
        if (swiftCtxt.isRibautSelected()) {
            RibautBeneficiaryDetailsBO ribautBenefBO = swiftCtxt.getRibautBeneficiaryDetailsBO();
            if (ribautBenefBO != null) {
                swiftCtxt.setBeneficiaryBankName(ribautBenefBO.getBeneficiaryBankName());
                swiftCtxt.setBeneficiaryName(StringUtils.substringSafelyToLength(StringUtils.concatenate(" ".toCharArray(),
                                                                                                         ribautBenefBO.getBeneficiaryFirstName(),
                                                                                                         ribautBenefBO.getBeneficiarylastName()), 0,
                                                                                 70));
                swiftCtxt.setBeneficiaryIban(ribautBenefBO.getBeneficiaryAccount());
                DetailedClientBO clientBO = new DetailedClientBO();
                clientBO.setClientId(ribautBenefBO.getClientId());
                clientBO.setClientName(ribautBenefBO.getClientName());
                swiftCtxt.setDebitClientBO(clientBO);
            }
        }

        return swiftCtxt;
    }

    public ClearingTransferContextBO getGenerateClearingTransferContextBO(ExternalTransferContextBO externalTransferContextBO) {
        ClearingTransferContextBO ctx = new ClearingTransferContextBO(externalTransferContextBO);
        setCoreBankingLanguage(ctx.getUserLanguage());

        if (ctx.isCallingTfReceivedSwift() && ctx.getReceivedSwiftTransferBO() != null) {
            ctx.fillContextFromReceivedSwift(); // the received swift is stored inside the BO
        } else if (ctx.isCallingTfInternetTransfer() && ctx.getInternetTransferBO() != null) {
            ctx.fillContextFromInternetTransfer();
        }

        ClearingTransferBO initialTransfer = null;
        if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) { // if modify or duplicate
            initialTransfer =
                getClearingTransferBO(ctx.getOperationReference(), ctx.getUser().getUsername(), ctx.getTransferType().getV2Applic(),
                                      ctx.getUserLanguage());

            if (initialTransfer == null)
                ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF", ctx.getUserLanguage()));
            else {
                List<GlobalCommissionBO> initialComissionList =
                    getOperationCommissionList(ctx.getOperationReference(), initialTransfer.getDebitAmount(), initialTransfer.getCreditAmount(),
                                               initialTransfer.getDebitAccount(), initialTransfer.getCreditAccount(), ctx.getUser().getUsername(),
                                               ctx.getTransferType().toString(), ctx.getUserLanguage(), ctx.getWizardMode());
                ctx.fillContextObject(initialTransfer, initialComissionList);
            }
        }

        //get the user authorization on operation
        GetUserOperationAuthorizationResponse getUserOperAuthResp =
            paymentsTransferCommonModelSessionEJBLocal.getUserOperationAuthorization(ctx.getTransferType().getV2Applic(),
                                                                                     ctx.getLegacyUser().getCserv(), ctx.getLegacyUser().getExpl());
        ctx.getErrorResponse().handlePLSQLFunctionResponse(getUserOperAuthResp);

        if (!getUserOperAuthResp.hasError()) {
            ctx.getLegacyUser().setAuthorizedOperationList(getUserOperAuthResp.getAuthorizedOperationList());
            ctx.getLegacyUser().setUnauthorizedOperationList(getUserOperAuthResp.getUnauthorizedOperationList());
        }

        GetRibInfoResponse resp = getRibInfo(ctx.getUser().getUsername(), ctx.getTransferType().toString(), ctx.getUserLanguage());

        ctx.getErrorResponse().handlePLSQLFunctionResponse(resp, ExternalTransferValidationEnum.AccountKey);
        if (!resp.hasError()) {
            String keyAccess = "Y".equals(resp.getRibMandatory()) ? "REQ" : "OFF";
            ctx.setAccAccountRib(keyAccess);
        }

        //get chequenumber control attributes
        //Get cheque number attributes
        GetChequeNumberAttributesResponse getCheckNumberAttributesResponse =
            paymentsTransferCommonModelSessionEJBLocal.getChequeNumberAttributes(ctx.getTransferType().toString());
        ctx.getErrorResponse().handlePLSQLFunctionResponse(getCheckNumberAttributesResponse);

        ctx.setChequeNumberAttributes(getCheckNumberAttributesResponse);


        //Control RibautBeneficiaryDetails
        if (ctx.isRibautSelected()) {
            RibautBeneficiaryDetailsBO ribautBenefBO = ctx.getRibautBeneficiaryDetailsBO();
            if (ribautBenefBO != null) {
                ctx.setBankCodeBO(new BankCodeBO(ribautBenefBO.getBankCode()));
                ctx.setBeneficiaryBankName(ribautBenefBO.getBeneficiaryBankName());
                ctx.setBeneficiaryName(StringUtils.substringSafelyToLength(StringUtils.concatenate(" ".toCharArray(),
                                                                                                   ribautBenefBO.getBeneficiaryFirstName(),
                                                                                                   ribautBenefBO.getBeneficiarylastName()), 0, 70));
                ctx.setAccount(ribautBenefBO.getBeneficiaryAccount());
                DetailedClientBO clientBO = new DetailedClientBO();
                clientBO.setClientId(ribautBenefBO.getClientId());
                clientBO.setClientName(ribautBenefBO.getClientName());
                ctx.setDebitClientBO(clientBO);
            }
        }
        return ctx;
    }


    public SepaTransferContextBO getGenerateSepaTransferContextBO(ExternalTransferContextBO externalTransferContextBO) {
        SepaTransferContextBO ctx = new SepaTransferContextBO(externalTransferContextBO);
        ctx.setTransferType(TransferTypeEnum.Sepa);
        setCoreBankingLanguage(ctx.getUserLanguage());

        if (ctx.isCallingTfReceivedSwift() && ctx.getReceivedSwiftTransferBO() != null) {
            ctx.fillContextFromReceivedSwift(); // the received swift is stored inside the BO
        } else if (ctx.isCallingTfInternetTransfer() && ctx.getInternetTransferBO() != null) {
            ctx.fillContextFromInternetTransfer();
        }

        SepaTransferBO initialTransfer = null;
        if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) {
            initialTransfer =
                getSepaTransferBO(ctx.getOperationReference(), ctx.getUser().getUsername(), ctx.getTransferType().toString(), ctx.getUserLanguage());
            if (initialTransfer == null)
                ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF", ctx.getUserLanguage()));
            else {
                List<GlobalCommissionBO> initialComissionList =
                    getOperationCommissionList(ctx.getOperationReference(), initialTransfer.getDebitAmount(), initialTransfer.getCreditAmount(),
                                               initialTransfer.getDebitAccount(), initialTransfer.getCreditAccount(), ctx.getUser().getUsername(),
                                               ctx.getTransferType().toString(), ctx.getUserLanguage(), ctx.getWizardMode());


                ctx.fillContextObject(initialTransfer, initialComissionList);
            }
        }

        //get the user authorization on operation
        GetUserOperationAuthorizationResponse getUserOperAuthResp =
            paymentsTransferCommonModelSessionEJBLocal.getUserOperationAuthorization(ctx.getTransferType().getV2Applic(),
                                                                                     ctx.getLegacyUser().getCserv(), ctx.getLegacyUser().getExpl());
        ctx.getErrorResponse().handlePLSQLFunctionResponse(getUserOperAuthResp);

        if (!getUserOperAuthResp.hasError()) {
            ctx.getLegacyUser().setAuthorizedOperationList(getUserOperAuthResp.getAuthorizedOperationList());
            ctx.getLegacyUser().setUnauthorizedOperationList(getUserOperAuthResp.getUnauthorizedOperationList());
        }

        //Control RibautBeneficiaryDetails
        if (ctx.isRibautSelected()) {
            RibautBeneficiaryDetailsBO ribautBenefBO = ctx.getRibautBeneficiaryDetailsBO();
            if (ribautBenefBO != null) {
                ctx.setBeneficiaryBankName(ribautBenefBO.getBeneficiaryBankName());
                ctx.setBeneficiaryName(StringUtils.substringSafelyToLength(StringUtils.concatenate(" ".toCharArray(),
                                                                                                   ribautBenefBO.getBeneficiaryFirstName(),
                                                                                                   ribautBenefBO.getBeneficiarylastName()), 0, 70));
                ctx.setBeneficiaryIban(ribautBenefBO.getBeneficiaryAccount());
                DetailedClientBO clientBO = new DetailedClientBO();
                clientBO.setClientId(ribautBenefBO.getClientId());
                clientBO.setClientName(ribautBenefBO.getClientName());
                ctx.setDebitClientBO(clientBO);
            }
        }
        return ctx;
    }

    public SwiftTransferContextBO getSwiftTransferContextBONext1(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            //clear all previous error messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(ctx.getUserLanguage());

            //validate that the iban is valid (in case we are in iban)
            if (ctx.getBeneficiaryBankCountryBO() != null && "O".equalsIgnoreCase(ctx.getIbanControl())) {

                ctx.setBeneficiaryIban(ctx.getBeneficiaryIban());

                if (ctx.getBeneficiaryBankCountryBO() != null && ctx.getBeneficiaryIban() != null && !ctx.isCallingTfReceivedSwift()) {
                    CheckIbanValidResponse ibanValidResponse =
                        getCheckIbanValid(ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars(), ctx.getBeneficiaryIban(), ctx.getUser().getUsername(),
                                          TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());

                    ctx.getErrorResponse().handlePLSQLFunctionResponse(ibanValidResponse, ExternalTransferValidationEnum.Iban);
                }
            }

            // Calculate maximum allowed transfer date
            Date maxAllowedTransferDate =
                getMaxAllowedTransferDate(ctx.getBankState().getDevref(), ctx.getBankState().getCurrentDate(), TransferTypeEnum.Swift.toString());
            ctx.setCtrlMaxAllowedTransferDate(maxAllowedTransferDate);
            ctx.setCtrlMinAllowedTransferDate(ctx.getBankState().getCurrentDate());

            // Get the default transfer mode
            if (ctx.getTransferMode() == null && PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode()) &&
                !ctx.isCallingTfReceivedSwift()) {

                String dftTransferModeCode = getDefaultTransferModeCode(TransferTypeEnum.Swift.toString(), ctx.getUser().getServiceCode());
                if (dftTransferModeCode != null)
                    ctx.setTransferMode(getSwiftTransferMode(null, dftTransferModeCode));
            }

            // Cheque number controls
            if (ctx.getChequeAttributes() == null)
                ctx.setChequeAttributes(getChequeAttributes(TransferTypeEnum.Swift.toString()));

            //setting intial values for step 2
            if (!PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !ctx.isCallingTfReceivedSwift())
                if (ctx.getClientRelativeReference() == null)
                    ctx.setClientRelativeReference(ctx.DEFAULT_CLIENT_REF_VALUE);

            if (PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode()) && !ctx.isCallingTfReceivedSwift()) {
                if (ctx.getSwiftPeriodicity() == null)
                    ctx.setSwiftPeriodicity(getSwiftTransferPeriodicity("U"));

                //Set default value for ordering party on SWIFT
                if (ctx.getOrderingPartyOnSwift() == null)
                    ctx.setOrderingPartyOnSwift("N");
            }

            if (ctx.isCallingTfReceivedSwift()) {
                if (!StringUtils.isEmpty(ctx.getInfoToBeneficiaryBankText()) && ctx.getInfoToBeneficiaryBankSwiftAbr() == null) {
                    ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.InformationToBeneficiaryBankSwiftAbbreviation,
                                                        translate("INFO_BENEF_BANK_REQ", ctx.getUserLanguage()));
                }
            }

        }
        return ctx;
    }

    public SwiftTransferContextBO getSwiftTransferContextBONext2(SwiftTransferContextBO swiftTransferContextBO) {

        SwiftTransferContextBO ctx = swiftTransferContextBO;

        if (ctx != null) {
            //clear all previous error messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(ctx.getUserLanguage());

            //save the debit client in the client entity
            if (ctx.getDebitClientBO() != null) {
                Client client = findClientById(ctx.getDebitClientBO().getClientId());
                ctx.setClient(client);

                if (client != null) {
                    GetOrderingPartyNameAndCityResponse getOrderingPartyNameAndCityResponse =
                        getOrderingPartyNameAndCity(client.getClient(), null, null);

                    ctx.setOrderingPartyNameAndCityResponse(getOrderingPartyNameAndCityResponse);
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(getOrderingPartyNameAndCityResponse);
                }
            }

            boolean hasDebitAccountChanged = true;
            boolean hasChequeNumberChanged = true;
            if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                if (ctx.getDebitAccount() != null && ctx.getInitialTransfer() != null) {
                    hasDebitAccountChanged = !Objects.equals(ctx.getDebitAccount().getAccountNumber(), ctx.getInitialTransfer().getDebitAccount());
                    hasChequeNumberChanged = !Objects.equals(ctx.getChequeNumber(), ctx.getInitialTransfer().getChequeNumber());
                }
            }

            //recontrol on exchange rate
            if (ctx.getAmount() != null && ctx.getTransferCurrency() != null && ctx.getDebitAccount() != null &&
                ctx.getDebitAccount().getCurrency() != null && ctx.getExchangeRate() != null && !ctx.isCallingTfReceivedSwift()) {
                CalculateAmountCounterValueResponse calcMntCountervalueResp =
                    getCalculateAmountCounterValue(ctx.getTransferCurrency().getIsoCode(), ctx.getAmount(),
                                                   ctx.getDebitAccount().getCurrency().getIsoCode(), ctx.getDebitAccount().getCurrency().getIsoCode(),
                                                   ctx.getExchangeRate(), ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                                   swiftTransferContextBO.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(calcMntCountervalueResp, ExternalTransferValidationEnum.ExchangeRate);

                if (ctx.hasError())
                    return ctx;

                if (!ctx.getStrongCurrencyIsoCode().equals(calcMntCountervalueResp.getStrongCurrencyIsoCode()) ||
                    !ctx.getWeakCurrencyIsoCode().equals(calcMntCountervalueResp.getWeakCurrencyIsoCode())) {

                    ctx.getErrorResponse().addWarningText(translate("MSG_CCY_RESET", swiftTransferContextBO.getUserLanguage()));
                    ctx.setStrongCurrencyIsoCode(calcMntCountervalueResp.getStrongCurrencyIsoCode());
                    ctx.setWeakCurrencyIsoCode(calcMntCountervalueResp.getWeakCurrencyIsoCode());
                    ctx.setExchangeRate(calcMntCountervalueResp.getRate());
                    ctx.setCounterValueAmount(calcMntCountervalueResp.getCounterValueAmount());
                }
            }

            //If in modification mode and if the transfer currency was changed, reset the correspondent info
            if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode()) && ctx.getInitialTransfer() != null &&
                ctx.getTransferCurrency() != null &&
                !ctx.getInitialTransfer().getCreditCurrencyCode().equals(ctx.getTransferCurrency().getIsoCode())) {
                ctx.setCorrespondantBic(null);
                ctx.setCorrespondent(null);
            }

            //control on weekend dates
            String stateCountryIsoCode = ctx.getBankState().getCountry().getIsoCode2Chars();
            Currency debitAccountCurrency = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getCurrency() : null;
            String debitCurrencyIsoCode = debitAccountCurrency != null ? debitAccountCurrency.getIsoCode() : "";

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) &&
                  ctx.getOperationDate().equals(ctx.getInitialTransfer().getOperationDate()))) {

                if (swiftTransferContextBO.getOperationDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkOperationDateWeekendResp =
                        getCheckIfDateIsOnWeekend(swiftTransferContextBO.getOperationDate(), stateCountryIsoCode, debitCurrencyIsoCode);

                    if (checkOperationDateWeekendResp != null && checkOperationDateWeekendResp.getIsOnWeekend()) {
                        swiftTransferContextBO.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.OperationDate,
                                                                                         translate("PROMPT_HOLIDAY_1",
                                                                                                   swiftTransferContextBO.getUserLanguage()) + " " +
                                                                                         translate("OPERATION_DATE",
                                                                                                   swiftTransferContextBO.getUserLanguage()) + " " +
                                                                                         translate("QUESTION_MARK",
                                                                                                   swiftTransferContextBO.getUserLanguage()));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) &&
                  Objects.equals(ctx.getFirstExecutionDate(), ctx.getInitialTransfer().getNextExecutionDate()))) {

                if (swiftTransferContextBO.getFirstExecutionDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkFirstExecDateWeekendResp =
                        getCheckIfDateIsOnWeekend(swiftTransferContextBO.getFirstExecutionDate(), stateCountryIsoCode, debitCurrencyIsoCode);

                    if (checkFirstExecDateWeekendResp != null && checkFirstExecDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FirstExecutionDate,
                                                                      translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                      translate("EXECUTION_DATE", ctx.getUserLanguage()) + " " +
                                                                      translate("QUESTION_MARK", ctx.getUserLanguage()));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) &&
                  Objects.equals(ctx.getFinalMaturityDate(), ctx.getInitialTransfer().getFinalMaturityDate()))) {

                if (ctx.getFinalMaturityDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkFinalMaturityDateWeekendResp =
                        getCheckIfDateIsOnWeekend(ctx.getFinalMaturityDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                    if (checkFinalMaturityDateWeekendResp != null && checkFinalMaturityDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FinalMaturityDate,
                                                                      MessageFormat.format("{0} {1} {2}",
                                                                                           translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()),
                                                                                           translate("MATURITY_DATE", ctx.getUserLanguage()),
                                                                                           translate("QUESTION_MARK", ctx.getUserLanguage())));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) &&
                  Objects.equals(ctx.getDebitValueDate(), ctx.getInitialTransfer().getDebitValueDate()))) {

                if (swiftTransferContextBO.getDebitValueDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkDebitValueDateWeekendResp =
                        getCheckIfDateIsOnWeekend(swiftTransferContextBO.getDebitValueDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                    if (checkDebitValueDateWeekendResp != null && checkDebitValueDateWeekendResp.getIsOnWeekend()) {
                        swiftTransferContextBO.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.DebitValueDate,
                                                                                         translate("PROMPT_HOLIDAY_1",
                                                                                                   swiftTransferContextBO.getUserLanguage()) + " " +
                                                                                         translate("DEBIT_VALUE_DATE",
                                                                                                   swiftTransferContextBO.getUserLanguage()) + " " +
                                                                                         translate("QUESTION_MARK",
                                                                                                   swiftTransferContextBO.getUserLanguage()));
                    }
                }
            }

            //control on ordering bic
            if (ctx.getOrderingPartyBic() != null && "1".equals(StringUtils.substringSafelyToLength(ctx.getOrderingPartyBic(), 7, 1)))
                ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.OrderingPartyBic,
                                                    translate("MSG_INVALID_BIC", swiftTransferContextBO.getUserLanguage()));

            //Control on transfer exchange reference
            String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;

            if (ctx.getStrongCurrencyIsoCode() != null && ctx.getWeakCurrencyIsoCode() != null && ctx.getCounterValueAmount() != null &&
                ctx.getDebitAccount() != null) {
                GetExchangeAuthorizedLimitResponse checkExchangeAuthorizedLimit =
                    paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAuthorizedLimit(ctx.getStrongCurrencyIsoCode(),
                                                                                                      ctx.getWeakCurrencyIsoCode(), null,
                                                                                                      exchangeReference, ctx.getCounterValueAmount(),
                                                                                                      ctx.getDebitAccount().getCurrency().getIsoCode(),
                                                                                                      ctx.getLegacyUser().getAcces(),
                                                                                                      ctx.getBankState().getDevref(),
                                                                                                      ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkExchangeAuthorizedLimit);
            }

            if (exchangeReference != null && ctx.getCounterValueAmount() != null) {
                GetExchangeAmountResponse getExchangeAmountResponse =
                    paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAmount(exchangeReference, ctx.getCounterValueAmount());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getExchangeAmountResponse);
            }

            //now starting ui package for virest next2
            CheckIsTillModelExistentResponse checkIsTillModelExsitentResponse = checkIsTillModelExistent();

            CheckIsTillControlledForSwiftTransferModeResponse checkIsTillControlledForSwiftTransferModeResponse =
                checkIsTillControlledForSwiftTransferMode(swiftTransferContextBO.getTransferMode().getCode());

            if (checkIsTillModelExsitentResponse != null && checkIsTillModelExsitentResponse.getIsTillModelExistent() &&
                checkIsTillControlledForSwiftTransferModeResponse != null &&
                checkIsTillControlledForSwiftTransferModeResponse.getIsTillControlled()) {

                //read the till variables for the user logged in : this is taken from pk_capb_ui_et_virest.step2

                String userTill = getCashierClient(swiftTransferContextBO.getUser().getUsercode());
                String branchTill = getCashierClient(swiftTransferContextBO.getUser().getBranchCode());

                if (!swiftTransferContextBO.getClient().getClient().equalsIgnoreCase(userTill) &&
                    !swiftTransferContextBO.getClient().getClient().equalsIgnoreCase(branchTill)) {
                    swiftTransferContextBO.getErrorResponse().addErrorText(translate("PROMPT_TILL_CONTROL_1",
                                                                                     swiftTransferContextBO.getUserLanguage()) + " " + userTill +
                                                                           " " +
                                                                           translate("PROMPT_TILL_CONTROL_2",
                                                                                     swiftTransferContextBO.getUserLanguage()) + " " + branchTill);
                }

            }
            // TODO: contine here

            String debitAccountType = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getTyp() : null;
            //ES 20180504 CBKS-1576
            String account50k = (String) ctx.getAccount50k();
            if (!"I".equalsIgnoreCase(debitAccountType) && !ctx.isCallingTfReceivedSwift() && StringUtils.isEmpty(account50k)) {
                GetAccount50kFlagResponse getAccount50kFlagResponse = getAccount50kFlag();
                swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(getAccount50kFlagResponse);

                //return at this stage in case of error
                if (swiftTransferContextBO.hasError())
                    return swiftTransferContextBO;

                String account50kFlag = getAccount50kFlagResponse != null ? getAccount50kFlagResponse.getAccount50kFlag() : null;
                String _account50k = null;

                if ("O".equalsIgnoreCase(account50kFlag))
                    _account50k =
                        swiftTransferContextBO.getDebitAccount().getCptdeve() == null ? swiftTransferContextBO.getDebitAccount().getAccountNumber() :
                        StringUtils.substringSafelyToLength(swiftTransferContextBO.getDebitAccount().getCptdeve().trim(), 0, 11);
                else
                    _account50k = swiftTransferContextBO.getDebitAccount() != null ? swiftTransferContextBO.getDebitAccount().getAccountNumber() : "";

                if (swiftTransferContextBO.getBankState().getCountry().getIbanLength() != null && swiftTransferContextBO.getDebitAccount() != null) { //instead of test_cee_iban
                    CalculateRibKeyResponse calculateRibKeyResponse = calculateRibKey(swiftTransferContextBO.getBankState().getCodbnq(), swiftTransferContextBO.getDebitAccount().getBranchCode() //TODO the second parametere is counter code, but in UI package plsql it is ui.gAgenced, to be checked later on
                                                                                      , _account50k);
                    swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(calculateRibKeyResponse);

                    //Continue only if there is no error
                    if (!calculateRibKeyResponse.hasError()) {
                        String ribKey = calculateRibKeyResponse.getRibKey();

                        CalculateIbanFromRibKeyResponse calculateIbanFromRibKeyResponse =
                            calculateIbanFromRibKey(swiftTransferContextBO.getBankState().getCodbnq(), swiftTransferContextBO.getDebitAccount().getBranchCode() //TODO same comment of this parameter as above
                                                    , _account50k, ribKey);
                        swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(calculateIbanFromRibKeyResponse);

                        if (!calculateIbanFromRibKeyResponse.hasError()) {
                            String tempAccount50k = calculateIbanFromRibKeyResponse.getIban();
                            swiftTransferContextBO.setAccount50k(tempAccount50k);
                        }
                    } //end of no error

                } else //if not cee iban (iban not activated)
                    swiftTransferContextBO.setAccount50k(_account50k);

                if (swiftTransferContextBO.hasError())
                    return swiftTransferContextBO;
            }

            if (swiftTransferContextBO.getTransferCurrency() != null && swiftTransferContextBO.getAmount() != null &&
                swiftTransferContextBO.getDebitAccount() != null) {
                CheckIsAmountInLedgerLimitResponse checkIsAmountInLedgerLimitResponse =
                    checkIsAmountInLedgerLimit(swiftTransferContextBO.getAmount(), swiftTransferContextBO.getDebitAccount().getAvailableBalance(),
                                               swiftTransferContextBO.getTransferCurrency().getIsoCode(),
                                               swiftTransferContextBO.getDebitAccount().getLedger().getLedger());
                swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(checkIsAmountInLedgerLimitResponse);
            }

            //check operation position code (Xposoper)
            if (ctx.getDebitAccount() != null &&
                ctx.getCounterValueAmount() !=
                                  null) {
                //ES 20180412 CBKSE-734 In modify mode, reference number should be sent
                GetOperOverdraftInfoResponse getOverdraftInfoResp = new GetOperOverdraftInfoResponse();
                //ES    CBKS-1744 Controlling the overdraft warning depending on user's location
                CheckUserAtHeadOfficeResponse checkUserAtHeaderResponse =
                       checkUserAtHeadOffice(ctx.getOperationReference(), ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                             ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkUserAtHeaderResponse);
                if (checkUserAtHeaderResponse != null && !checkUserAtHeaderResponse.hasError()) {

                    if ((!"O".equalsIgnoreCase(checkUserAtHeaderResponse.getIsHeadOffice()) &&
                         !"OV".equalsIgnoreCase(checkUserAtHeaderResponse.getIsHeadOffice())) ||
                        !PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                        if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                            getOverdraftInfoResp =
                                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(ctx.getOperationReference(),
                                                                                                ctx.getDebitAccount().getAccountNumber(),
                                                                                                ctx.getCounterValueAmount(),
                                                                                                ctx.getUser().getUsername(),
                                                                                                TransferTypeEnum.Swift.toString(),
                                                                                                ctx.getUserLanguage());
                            //ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
                        } else {
                            getOverdraftInfoResp =
                                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, ctx.getDebitAccount().getAccountNumber(),
                                                                                                ctx.getCounterValueAmount(),
                                                                                                ctx.getUser().getUsername(),
                                                                                                TransferTypeEnum.Swift.toString(),
                                                                                                ctx.getUserLanguage());
                        }
                    }

                    ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
                    if (getOverdraftInfoResp != null && !getOverdraftInfoResp.hasError()) {
                        ctx.setOperationPositionCodeTmp(getOverdraftInfoResp.getOperPositionCode());
                        if ("C".equals(getOverdraftInfoResp.getOperPositionCode()))
                            ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_OVERDRAFT", ctx.getUserLanguage()));
                        else if ("B".equals(getOverdraftInfoResp.getOperPositionCode()))
                            ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_NOT_OVERDRAFT", ctx.getUserLanguage()));
                    }
                }
            }

            //source: ordering p name.WHEN_VALIDATE_ITEM
            String orderingPartyCountryIso =
                swiftTransferContextBO.getClient() != null && swiftTransferContextBO.getClient().getTaxCountry() != null ?
                swiftTransferContextBO.getClient().getTaxCountry().getIsoCode2Chars() : null;
            String orderingPartyPostalCode =
                swiftTransferContextBO.getDefaultOrderingPartyAttributes() != null ?
                swiftTransferContextBO.getDefaultOrderingPartyAttributes().getPostalCode() : null;

            if (orderingPartyCountryIso != null && orderingPartyPostalCode != null && swiftTransferContextBO.getDebitAccount() != null) {
                CheckIs50fValidResponse checkIs50fValidResponse =
                    checkIs50fValid(orderingPartyCountryIso, swiftTransferContextBO.getAccount50k(),
                                    swiftTransferContextBO.getDebitAccount().getAccountNumber(), "A.DORDRED",
                                    swiftTransferContextBO.getOrderingPartyAddress1(), swiftTransferContextBO.getOrderingPartyAddress2(),
                                    swiftTransferContextBO.getOrderingPartyName1(), swiftTransferContextBO.getOrderingPartyName2(),
                                    orderingPartyPostalCode);
                swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(checkIs50fValidResponse);

                if (swiftTransferContextBO.getDebitAccount() != null) {
                    checkIs50fValidResponse =
                        checkIs50fValid(orderingPartyCountryIso, swiftTransferContextBO.getAccount50k(),
                                        swiftTransferContextBO.getDebitAccount().getAccountNumber(), "A.DORDRED2",
                                        swiftTransferContextBO.getOrderingPartyAddress1(), swiftTransferContextBO.getOrderingPartyAddress2(),
                                        swiftTransferContextBO.getOrderingPartyName1(), swiftTransferContextBO.getOrderingPartyName2(),
                                        orderingPartyPostalCode);
                    swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(checkIs50fValidResponse);
                } //end of ui package for virest next2
            }

            // back to ui package external
            if (swiftTransferContextBO.getTransferMode() != null && swiftTransferContextBO.getSwiftPeriodicity() != null) {
                GetChequeNumAndRelativeRefAccessInfoResponse getCheckNumAndRelativeRefAccessInfoResponse =
                    getChequeNumAndRelativeRefAccessInfo(swiftTransferContextBO.getTransferMode().getCode(),
                                                         swiftTransferContextBO.getSwiftPeriodicity().getCode(),
                                                         swiftTransferContextBO.getDebitAccount().getLornos(),
                                                         swiftTransferContextBO.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                                         swiftTransferContextBO.getUserLanguage());
                swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(getCheckNumAndRelativeRefAccessInfoResponse);

                if (getCheckNumAndRelativeRefAccessInfoResponse != null && !getCheckNumAndRelativeRefAccessInfoResponse.hasError()) {
                    swiftTransferContextBO.setChequeNumAndRefRelAccessInfo(getCheckNumAndRelativeRefAccessInfoResponse);
                    //setting the access rights

                    swiftTransferContextBO.setAccRelativeReference(getCheckNumAndRelativeRefAccessInfoResponse.getAccRelativeRef());
                    swiftTransferContextBO.setAccChequeNumber(getCheckNumAndRelativeRefAccessInfoResponse.getAccChequeNumber());

                    //validate cheque number and periodicity
                    if ("Y".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) &&
                        swiftTransferContextBO.getChequeAttributes() != null &&
                        swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList() != null &&
                        swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList().contains(ctx.getTransferMode().getCode()) &&
                        !"U".equals(ctx.getSwiftPeriodicity().getCode()) && ctx.getChequeNumber() != null) {

                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.ChequeNumber,
                                                            translate("MSG_INVALID_NUMCHQ_PERIOD", swiftTransferContextBO.getUserLanguage()));
                    }

                    if (ctx.hasError())
                        return ctx;

                    if ("O".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberControlled()) &&
                        "Y".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) &&
                        swiftTransferContextBO.getChequeAttributes() != null &&
                        swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList() != null &&
                        swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList().contains(swiftTransferContextBO.getTransferMode().getCode())) {
                        if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasDebitAccountChanged &&
                              !hasChequeNumberChanged) && swiftTransferContextBO.getChequeNumber() != null) {
                            ControlChequeNumberResponse controlChequeNumberResponse =
                                controlChequeNumber(swiftTransferContextBO.getDebitAccount().getAccountNumber(),
                                                    swiftTransferContextBO.getChequeNumber(), TransferTypeEnum.Swift.toString());
                            swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(controlChequeNumberResponse,
                                                                                                  ExternalTransferValidationEnum.ChequeNumber);
                        }
                    }

                    //Control on ordering party reference (REFDEB)
                    if (("N".equals(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) ||
                         (swiftTransferContextBO.getChequeAttributes() != null &&
                          swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList() != null &&
                          !swiftTransferContextBO.getChequeAttributes().getChequeTransferModeList().contains(swiftTransferContextBO.getTransferMode().getCode()))) &&
                        "Q".equals(swiftTransferContextBO.getTransferMode().getCode()) &&
                        !"NEW".equals(swiftTransferContextBO.getOrderingPartyReference())) {

                        ControlChequeRefForModeQResponse resp =
                            controlChequeRefForModeQ(swiftTransferContextBO.getDebitAccount().getAccountNumber(),
                                                     swiftTransferContextBO.getOrderingPartyReference(),
                                                     swiftTransferContextBO.getOperationReference(),
                                                     swiftTransferContextBO.getTransferType().toString());
                        swiftTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(resp,
                                                                                              ExternalTransferValidationEnum.OrderingPartyReference);
                    }
                }
            }

            //set default values for the next 3 step
            if (swiftTransferContextBO.getBankOperationCode() == null && PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode()))
                swiftTransferContextBO.setBankOperationCode(findSwiftAbbreviationByFieldAndAbbr("23", "B/CRED"));

            validateSwiftTarget(ctx);
        } //end of if swiftContextBO != null
        return swiftTransferContextBO;
    }

    public SwiftTransferContextBO getSwiftTransferContextBONext3(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            //rest all messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(ctx.getUserLanguage());

            //Check if debit and credit account are the same
            if (ctx.getDebitAccount().getAccountNumber().equalsIgnoreCase(ctx.getCorrespondent().getAccount()))
                ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CorrespondentAccount,
                                                              translate("MSG_SAME_DEBIT_CREDIT_ACCOUNT", ctx.getUserLanguage()));

            String correspondentCountryIso = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getCountry() : null;
            String correspondentCurrencyIso = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getCurrency() : "";

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && ctx.getInitialTransfer() != null &&
                  ctx.getCreditValueDate() != null && Objects.equals(ctx.getInitialTransfer().getCreditValueDate(), ctx.getCreditValueDate()))) {
                CheckIfDateIsOnWeekendResponse checkCreditValueDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getCreditValueDate(), correspondentCountryIso, correspondentCurrencyIso);
                if (checkCreditValueDateWeekendResp != null && checkCreditValueDateWeekendResp.getIsOnWeekend()) {
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CreditValueDate,
                                                                  translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                  translate("CREDIT_VALUE_DATE", ctx.getUserLanguage()) + " " +
                                                                  translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }

            String creditResidentCode = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccountClientResidence() : null;
            if (ctx.getBeneficiaryBankBic() == null) {
                if (ctx.getBeneficiaryBic() == null)
                    creditResidentCode = ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getResidenceTypeCode() : null;
                else if (ctx.getBeneficiaryBic() != null && ctx.getBeneficiaryBic().getCountry() != null &&
                         ctx.getBeneficiaryBic().getCountry().getResidenceType() != null)
                    creditResidentCode = ctx.getBeneficiaryBic().getCountry().getResidenceType().getCode();
            } else {
                BankBic beneficiryBankBic = findBankBicByBic(ctx.getBeneficiaryBankBic().getBicCode());
                if (beneficiryBankBic != null)
                    creditResidentCode =
                        beneficiryBankBic.getCountry() != null && beneficiryBankBic.getCountry().getResidenceType() != null ?
                        beneficiryBankBic.getCountry().getResidenceType().getCode() : null;
            }

            if (ctx.getTransferCurrency() != null) {

                String debitResidenceTypeCode = ctx.getClient() != null ? ctx.getClient().getResid() : null;

                String creditResidenceTypeCode = null;
                if (ctx.getBeneficiaryBic() != null) {
                    creditResidenceTypeCode =
                        (ctx.getBeneficiaryBic().getCountry() != null && ctx.getBeneficiaryBic().getCountry().getResidenceType() != null) ?
                        ctx.getBeneficiaryBic().getCountry().getResidenceType().getCode() : null;
                } else if (ctx.getBeneficiaryBankBic() != null) {
                    creditResidenceTypeCode = ctx.getBeneficiaryBankBic().getCountryResidCode();
                }

                GetPaymentReportingTypeResponse getPaymentReportingTypeResponse =
                    paymentReportingSessionEJB.getPaymentReportingActive(ctx.getCounterValueAmount(), ctx.getDebitAccount().getCurrencyCode(),
                                                                         debitResidenceTypeCode, creditResidenceTypeCode, ctx.getUser().getUsername(),
                                                                         TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getPaymentReportingTypeResponse);

                if (!getPaymentReportingTypeResponse.hasError()) {
                    ctx.setIsReportPaymentActive("O".equalsIgnoreCase(getPaymentReportingTypeResponse.getPaymentReportingActiveFlag()));
                    ctx.setReportPaymentTypeCode(getPaymentReportingTypeResponse.getPaymentReportingTypeCode());

                    if (getPaymentReportingTypeResponse.getPaymentReportingTypeCode() == null)
                        ctx.setIsReportPaymentActive(false);
                }
            }

            //start of the CRP function
            //Setting te parameters
            //This is not a mandatory parameter
            String beneficiaryBicCode = ctx.getBeneficiaryBic() != null ? ctx.getBeneficiaryBic().getBic() : null;
            String paymentReportActiveFlag = ctx.getIsReportPaymentActive() != null && ctx.getIsReportPaymentActive().booleanValue() ? "O" : "N";

            String infoToCorrespondent = ctx.getInfoToCorrespondent() != null ? ctx.getInfoToCorrespondent().getAbbreviation() : null;
            if (ctx.getInfoToCorrespondentText() != null)
                infoToCorrespondent += ctx.getInfoToCorrespondentText();

            if (ctx.getTransferMode() != null && ctx.getDebitAccount() != null && ctx.getCorrespondent() != null &&
                ctx.getTransferCurrency() != null && ctx.getBeneficiaryBankCountryBO() != null && ctx.getOrderingPartyNameAndCityResponse() != null) {
                GetPaymentReportDefaultValuesResponse reportPaymentDefaultValuesResponse =
                    getPaymentReportDefaultValuesResponse(ctx.getTransferMode().getCode(), paymentReportActiveFlag // "  crpActiveFlag "
                                                          , ctx.getDebitAccount().getAccountNumber(), ctx.getCorrespondent().getAccount() //String  creditAccountNumber
                                                          , ctx.getTransferCurrency().getIsoCode(), ctx.getAmount(), beneficiaryBicCode, ctx.getBeneficiaryBankCountryBO().getName() // TODO ML?  beneficiaryCityName
                                                          , ctx.getBeneficiaryBankBic() != null ? ctx.getBeneficiaryBankBic().getBicCode() : null // beneficiaryBankBicCode
                                                          , ctx.getDebitAccount().getLornos() //  p_i_DebitLornos
                                                          , ctx.getDebitAccount().getClient().getClient() //  String  debitClientId
                                                          , ctx.getCorrespondent().getAccountLornos() //   String  creditLornosCode
                                                          , ctx.getCorrespondent().getAccountClient() //  creditClientId
                                                          , ctx.getCorrespondantBic() // String  correspondentBicCode  TODO fix TYPO correspondant
                                                          , ctx.getCorrespondent().getAccountClientCountryName() //  correspondentCityName
                                                          , ctx.getReportPaymentTypeCode() //"String  reportPaymentTypeCode"
                                                          , ctx.getBeneficiaryIban() //  String  beneficiaryAccountNumber
                                                          , ctx.getBeneficiaryBankAccount() //   String  beneficiaryBankAccountNumber
                                                          , ctx.getBeneficiaryName() //   String  beneficiaryName
                                                          , null //"String  donorBicCode"  //This is null for external transfer. Later check it for other transfers
                                                          , ctx.getOrderingPartyNameAndCityResponse().getCityName() //"String  donorCityName"   //call a function findADR
                                                          , infoToCorrespondent, ctx.getDebitAccount().getTyp() // String  debitorAccountTypeCode
                                                          , null // this value is null following the UI package
                                                          , ctx.getClient().getResid() //String  debitorResidenceCode
                                                          , ctx.getCorrespondent().getAccountType() //TODO correct possible old bug in UI pakcage (look at rech_typcpt that is in the next3 create virest: called after this function)
                                                          , ctx.getClient().getSiren() // String  sirenCode
                                                          , ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString() //String applic
                                                          , ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(reportPaymentDefaultValuesResponse,
                                                                   ExternalTransferValidationEnum.CreditValueDate);

                if (!reportPaymentDefaultValuesResponse.hasError()) {
                    //setting the initial values for CRP
                    ctx.setPaymentReportCode(reportPaymentDefaultValuesResponse.getPaymentReportCode());
                    ctx.setPaymentReportCrpCode(reportPaymentDefaultValuesResponse.getPaymentReportCrpCode());
                    ctx.setPaymentReportCountry(reportPaymentDefaultValuesResponse.getCountry());
                    ctx.setPaymentReportSirenCode(reportPaymentDefaultValuesResponse.getSirenCode());
                    ctx.setPaymentReportBank(reportPaymentDefaultValuesResponse.getBankCode());
                    ctx.setPaymentReportBankCounter(reportPaymentDefaultValuesResponse.getBankCounter());
                    ctx.setPaymentReportResidenceType(reportPaymentDefaultValuesResponse.getCreditResidenceCode());
                }
            }

            //end of the CRP function
            //now start UI package virest nexst 3
            if (ctx.getBeneficiaryBankBic() != null) {
                BankBic beneficiaryBankBic = findBankBicByBic(ctx.getBeneficiaryBankBic().getBicCode());
                if (beneficiaryBankBic != null) {
                    if (beneficiaryBankBic.getCountry() != null &&
                        blockSwiftForCountryIso(correspondentCountryIso, beneficiaryBankBic.getCountry().getIsoCode2Chars()))
                        ctx.getErrorResponse().addErrorText(translate("MSG_ISO_CONTROL_PARAMETER", ctx.getUserLanguage()));
                }
            }
            String corresponentBicPublishedFlag = null;
            if (ctx.getCorrespondent() != null && ctx.getCorrespondent().getBic() != null) {
                if (StringUtils.substringSafelyToLength(ctx.getCorrespondent().getBic(), 7, 1).equalsIgnoreCase("1"))
                    ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_CORRESPONDENT_BIC", ctx.getUserLanguage()));
                corresponentBicPublishedFlag = ctx.getCorrespondent().getBicPublishedFlag();
            }
            corresponentBicPublishedFlag = corresponentBicPublishedFlag == null ? "N" : corresponentBicPublishedFlag;

            if ("O".equalsIgnoreCase(corresponentBicPublishedFlag) &&
                (ctx.getTransferCurrency() == null ||
                 (!"EUR".equalsIgnoreCase(ctx.getTransferCurrency().getIsoCode()) &&
                  !"GBP".equalsIgnoreCase(ctx.getTransferCurrency().getIsoCode()))))
                ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.TransferCurrency,
                                                    translate("MSG_TRSF_CUR_EUR_GBP", ctx.getUserLanguage()));

            String correspondentAccountType = ctx.getCorrespondent().getAccountType();
            if (!StringUtils.isEmpty(ctx.getBeneficiaryName()))
                correspondentAccountType =
                    ctx.getBeneficiaryName().toLowerCase().contains("ban") || ctx.getBeneficiaryName().toLowerCase().contains("bank") ||
                    ctx.getBeneficiaryName().toLowerCase().contains("banq") ? "B" : "C";
            ctx.setCorrespondentAccountTypeCode(correspondentAccountType);

            //calculate values for correspondent client name and address
            //TODO function name is to be reviewed
            if (ctx.getCorrespondent() != null) {
                GetOrderingPartyNameAndCityResponse getCorrespondentNameAndCityResponse =
                    getOrderingPartyNameAndCity(ctx.getCorrespondent().getAccountClient(), null, null);
                ctx.setCorrespondentNameAndCityResponse(getCorrespondentNameAndCityResponse);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getCorrespondentNameAndCityResponse);
            }

            if (ctx.isCallingTfReceivedSwift()) {
                if (!StringUtils.isEmpty(ctx.getInfoToCorrespondentText()) && ctx.getInfoToCorrespondent() == null) {
                    ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.InformationToCorrespondent,
                                                        translate("INFO_OUR_CORRESPONDENT_REQ", ctx.getUserLanguage()));
                }
            }
        }
        return ctx;
    }

    public SwiftTransferContextBO getSwiftTransferContextBONextCommissions(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            //rest all messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(ctx.getUserLanguage());

            //calculate and set Xposoper
            if (ctx.getDebitAccount() !=
                null) {
                //                GetOperOverdraftInfoResponse getOverdraftInfoResp =
                //                    paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null,
                //                                                                                    ctx.getDebitAccount().getAccountNumber(),
                //                                                                                    ctx.getDebitNetAmount(),
                //                                                                                    ctx.getUser().getUsername(),
                //                                                                                    TransferTypeEnum.Swift.toString(),
                //                                                                                    ctx.getUserLanguage());
                //                ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
                //ES 20180412 CBKSE-734 In modify mode, reference number should be sent
                GetOperOverdraftInfoResponse getOverdraftInfoResp = new GetOperOverdraftInfoResponse();
                //ES    CBKS-1744 Controlling the overdraft warning depending on user's location
                CheckUserAtHeadOfficeResponse checkUserAtHeaderResponse =
                                      checkUserAtHeadOffice(ctx.getOperationReference(), ctx.getUser().getUsername(),
                                                            TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkUserAtHeaderResponse);
                if (checkUserAtHeaderResponse != null && !checkUserAtHeaderResponse.hasError()) {
                    if ((!"O".equalsIgnoreCase(checkUserAtHeaderResponse.getIsHeadOffice()) &&
                         !"OV".equalsIgnoreCase(checkUserAtHeaderResponse.getIsHeadOffice())) ||
                        !PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                        if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                            getOverdraftInfoResp =
                                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(ctx.getOperationReference(),
                                                                                                ctx.getDebitAccount().getAccountNumber(),
                                                                                                ctx.getDebitNetAmount(), ctx.getUser().getUsername(),
                                                                                                TransferTypeEnum.Swift.toString(),
                                                                                                ctx.getUserLanguage());
                        } else {
                            getOverdraftInfoResp =
                                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, ctx.getDebitAccount().getAccountNumber(),
                                                                                                ctx.getDebitNetAmount(), ctx.getUser().getUsername(),
                                                                                                TransferTypeEnum.Swift.toString(),
                                                                                                ctx.getUserLanguage());
                        }
                    }
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);

                    if (getOverdraftInfoResp != null && !getOverdraftInfoResp.hasError()) {
                        ctx.setOperationPositionCode(getOverdraftInfoResp.getOperPositionCode());
                        if (!Objects.equals(ctx.getOperationPositionCode(), ctx.getOperationPositionCodeTmp())) {
                            if ("C".equals(getOverdraftInfoResp.getOperPositionCode()))
                                ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_OVERDRAFT", ctx.getUserLanguage()));
                            else if ("B".equals(getOverdraftInfoResp.getOperPositionCode()))
                                ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_NOT_OVERDRAFT", ctx.getUserLanguage()));
                        }
                    }
                }
            }

        }
        return ctx;
    }

    public SwiftTransferContextBO getSwiftTransferContextBONext4(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            setCoreBankingLanguage(ctx.getUserLanguage());

            ValidatePaymentReportingValuesResponse resp =
                paymentReportingSessionEJB.controlCrpCode(ctx.getPaymentReportingContextBO(), PaymentsExternalTransferModelConstants.CRP,
                                                          ctx.getUser().getUsername(), ctx.getTransferType().toString(), ctx.getUserLanguage());
            if (ctx.getErrorResponse() != null) {
                ctx.getErrorResponse().clearAllResponses();
                ctx.getErrorResponse().handlePLSQLFunctionResponse(resp);
            }
        }
        return ctx;
    }

    public SwiftTransferContextBO getSwiftTransferContextBOnext5(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            //rest all messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(ctx.getUserLanguage());

            String paymentReportCode = ctx.getPaymentReportCode() != null ? ctx.getPaymentReportCode().getCode() : null;
            String paymentReportBankCode = ctx.getPaymentReportBank() != null ? ctx.getPaymentReportBank().getCode() : null;
            String paymentReportCrpCode = ctx.getPaymentReportCrpCode() != null ? ctx.getPaymentReportCrpCode().getCode() : null;
            String paymentReportActiveFlag = ctx.getIsReportPaymentActive() != null && ctx.getIsReportPaymentActive().booleanValue() ? "O" : "N";

            if (paymentReportCode != null && paymentReportBankCode != null && paymentReportActiveFlag != null &&
                ctx.getReportPaymentTypeCode() != null && paymentReportCrpCode != null) {
                VerifyPaymentReportingValuesResponse verifyPaymentReportingValues =
                    verifyPaymentReportingValues(paymentReportCode, paymentReportBankCode, ctx.getPaymentReportSirenCode(), paymentReportActiveFlag,
                                                 ctx.getReportPaymentTypeCode(), paymentReportCrpCode, ctx.getUser().getUsername(),
                                                 TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(verifyPaymentReportingValues);
            }
        }
        return ctx;
    }

    //start Fucntions for the new approach1
    private ExternalTransferContextBO getExternalTransferContextBONext1A(ExternalTransferContextBO externalTransferContextBO) {
        //TODO : fix applic parameter
        String language = externalTransferContextBO.getUserLanguage();
        setCoreBankingLanguage(language);

        GetMaxAllowedOperDateResponse resp =
            getMaxAllowedOperDate(externalTransferContextBO.getTransferType().toString(), externalTransferContextBO.getBankState().getCurrentDate());
        externalTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(resp);

        if (!resp.hasError())
            externalTransferContextBO.setCtrlMaxAllowedTransferDate(resp.getMaxAllowedDate());

        externalTransferContextBO.setCtrlMinAllowedTransferDate(externalTransferContextBO.getBankState().getCurrentDate());

        String wizardMode = externalTransferContextBO.getWizardMode();

        if (PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode) && externalTransferContextBO.getPeriodicityBO() == null &&
            !externalTransferContextBO.isCallingTfReceivedSwift()) {
            TransferPeriodicityBO defaultPeriodicity = new TransferPeriodicityBO();
            defaultPeriodicity.setCode("U");
            externalTransferContextBO.setPeriodicityBO(defaultPeriodicity);
        }

        return externalTransferContextBO;
    }


    private ExternalTransferContextBO getExternalTransferContextBONext2A(ExternalTransferContextBO ctx) {
        setCoreBankingLanguage(ctx.getUserLanguage());

        if (ctx != null) {

            Client client = findClientById(ctx.getDebitClientBO().getClientId());
            ctx.setClient(client);

            //read the account entity
            Account debitAccount = findAccountByCode(ctx.getDebitAccountBO().getCode());
            ctx.setDebitAccount(debitAccount);

            //set default values for ordering party
            GetOrderingPartyNameAndCityResponse getOrderingPartyNameAndCityResponse = getOrderingPartyNameAndCity(client.getClient(), null, null);

            ctx.setOrderingPartyNameAndCityResponse(getOrderingPartyNameAndCityResponse);
            ctx.getErrorResponse().handlePLSQLFunctionResponse(getOrderingPartyNameAndCityResponse);

            //recontrol on exchange rate
            if (ctx.getAmount() != null && ctx.getTransferCurrencyBO() != null && ctx.getDebitAccount() != null &&
                ctx.getDebitAccount().getCurrency() != null && ctx.getExchangeRate() != null) {
                CalculateAmountCounterValueResponse calcMntCountervalueResp =
                    getCalculateAmountCounterValue(ctx.getTransferCurrencyBO().getCode(), ctx.getAmount(),
                                                   ctx.getDebitAccount().getCurrency().getIsoCode(), ctx.getDebitAccount().getCurrency().getIsoCode(),
                                                   ctx.getExchangeRate(), ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                                   ctx.getUserLanguage());

                ctx.getErrorResponse().handlePLSQLFunctionResponse(calcMntCountervalueResp, ExternalTransferValidationEnum.ExchangeRate);

                if (ctx.hasError())
                    return ctx;

                if (!ctx.getStrongCurrencyIsoCode().equals(calcMntCountervalueResp.getStrongCurrencyIsoCode()) ||
                    !ctx.getWeakCurrencyIsoCode().equals(calcMntCountervalueResp.getWeakCurrencyIsoCode())) {

                    ctx.getErrorResponse().addWarningText(translate("MSG_CCY_RESET", ctx.getUserLanguage()));
                    ctx.setStrongCurrencyIsoCode(calcMntCountervalueResp.getStrongCurrencyIsoCode());
                    ctx.setWeakCurrencyIsoCode(calcMntCountervalueResp.getWeakCurrencyIsoCode());
                    ctx.setExchangeRate(calcMntCountervalueResp.getRate());
                    ctx.setCounterValueAmount(calcMntCountervalueResp.getCounterValueAmount());
                }
            }

            Date initialTrsfOperDate = null;
            Date initialTrsfFirstExecDate = null;
            Date initialTrsfFinalMaturityDate = null;
            Date initialTrsfDebitValueDate = null;

            boolean hasOperationDateChanged = true;
            boolean hasFirstExecDateChanged = true;
            boolean hasFinalMaturityDateChanged = true;
            boolean hasDebitValueDateChanged = true;

            if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                if (ctx instanceof ClearingTransferContextBO) {
                    ClearingTransferContextBO clearingCtxt = (ClearingTransferContextBO) ctx;

                    ClearingTransferBO clearingTrsf = clearingCtxt.getInitialTransfer();

                    initialTrsfOperDate = clearingTrsf.getOperationDate();
                    initialTrsfFirstExecDate = clearingTrsf.getNextExecutionDate();
                    initialTrsfFinalMaturityDate = clearingTrsf.getFinalMaturityDate();
                    initialTrsfDebitValueDate = clearingTrsf.getDebitValueDate();
                } else if (ctx instanceof SepaTransferContextBO) {
                    SepaTransferContextBO sepaCtxt = (SepaTransferContextBO) ctx;
                    SepaTransferBO sepaTrsf = sepaCtxt.getInitialTransfer();
                    if (sepaTrsf != null) {
                        initialTrsfOperDate = sepaTrsf.getOperationDate();
                        initialTrsfFirstExecDate = sepaTrsf.getNextExecutionDate();
                        initialTrsfFinalMaturityDate = sepaTrsf.getFinalMaturityDate();
                        initialTrsfDebitValueDate = sepaTrsf.getDebitValueDate();
                    }
                }

                hasOperationDateChanged = !Objects.equals(ctx.getOperationDate(), initialTrsfOperDate);
                hasFirstExecDateChanged = !Objects.equals(ctx.getFirstExecutionDate(), initialTrsfFirstExecDate);
                hasFinalMaturityDateChanged = !Objects.equals(ctx.getFinalMaturityDate(), initialTrsfFinalMaturityDate);
                hasDebitValueDateChanged = !Objects.equals(ctx.getDebitValueDate(), initialTrsfDebitValueDate);
            }

            //control on weekend dates
            String stateCountryIsoCode = ctx.getBankState().getCountry().getIsoCode2Chars();
            String debitCurrencyIsoCode = ctx.getDebitAccountBO() != null ? ctx.getDebitAccountBO().getCurrencyIsoCode() : null;

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasOperationDateChanged)) {
                CheckIfDateIsOnWeekendResponse checkOperationDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getOperationDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                if (checkOperationDateWeekendResp != null && checkOperationDateWeekendResp.getIsOnWeekend()) {
                    if (ctx instanceof SepaTransferContextBO)
                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.OperationDate,
                                                            translate("OPERATION_DATE", ctx.getUserLanguage()) + " " +
                                                            translate("PROMPT_HOLIDAY_2", ctx.getUserLanguage()));

                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.OperationDate,
                                                                  translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                  translate("OPERATION_DATE", ctx.getUserLanguage()) + " " +
                                                                  translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasFirstExecDateChanged)) {
                CheckIfDateIsOnWeekendResponse checkFirstExecDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getFirstExecutionDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                if (checkFirstExecDateWeekendResp != null && checkFirstExecDateWeekendResp.getIsOnWeekend()) {
                    if (ctx instanceof SepaTransferContextBO)
                        //                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.FirstExecutionDate,
                        //                                                            translate("EXECUTION_DATE", ctx.getUserLanguage()) + " " +
                        //                                                            translate("PROMPT_HOLIDAY_2", ctx.getUserLanguage()));
















                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FirstExecutionDate,
                                                                      translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                      translate("EXECUTION_DATE", ctx.getUserLanguage()) + " " +
                                                                      translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasFinalMaturityDateChanged)) {
                CheckIfDateIsOnWeekendResponse checkFinalMaturityDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getFinalMaturityDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                if (checkFinalMaturityDateWeekendResp != null && checkFinalMaturityDateWeekendResp.getIsOnWeekend()) {
                    if (ctx instanceof SepaTransferContextBO) {
                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.FinalMaturityDate,
                                                            translate("MATURITY_DATE", ctx.getUserLanguage()) + " " +
                                                            translate("PROMPT_HOLIDAY_2", ctx.getUserLanguage()));
                    }
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FinalMaturityDate,
                                                                  translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                  translate("MATURITY_DATE", ctx.getUserLanguage()) + " " +
                                                                  translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasDebitValueDateChanged)) {
                CheckIfDateIsOnWeekendResponse checkDebitValueDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getDebitValueDate(), stateCountryIsoCode, debitCurrencyIsoCode);
                if (checkDebitValueDateWeekendResp != null && checkDebitValueDateWeekendResp.getIsOnWeekend()) {
                    if (ctx instanceof SepaTransferContextBO) {
                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.DebitValueDate,
                                                            translate("DEBIT_VALUE_DATE", ctx.getUserLanguage()) + " " +
                                                            translate("PROMPT_HOLIDAY_2", ctx.getUserLanguage()));
                    }
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.DebitValueDate,
                                                                  translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                  translate("DEBIT_VALUE_DATE", ctx.getUserLanguage()) + " " +
                                                                  translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }

            //Control on transfer exchange reference
            if (!ctx.isCallingTfReceivedSwift()) {
                String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;

                GetExchangeAuthorizedLimitResponse checkExchangeAuthorizedLimit =
                    paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAuthorizedLimit(ctx.getStrongCurrencyIsoCode(),
                                                                                                      ctx.getWeakCurrencyIsoCode(), null,
                                                                                                      exchangeReference, ctx.getCounterValueAmount(),
                                                                                                      ctx.getDebitAccountBO().getCurrencyIsoCode(),
                                                                                                      ctx.getLegacyUser().getAcces(),
                                                                                                      ctx.getBankState().getDevref(),
                                                                                                      ctx.getUserLanguage());

                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkExchangeAuthorizedLimit);

                if (exchangeReference != null) {
                    GetExchangeAmountResponse getExchangeAmountResponse =
                        paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAmount(exchangeReference, ctx.getCounterValueAmount());
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(getExchangeAmountResponse);
                }
            }
        } //end if null
        return ctx;
    }


    private ExternalTransferContextBO getExternalTransferContextBONext2B(ExternalTransferContextBO externalTransferContextBO) {
        //TODO Elio: Enhance the code
        ExternalTransferContextBO ctxt = externalTransferContextBO;
        String language = ctxt.getUserLanguage();
        setCoreBankingLanguage(language);
        if (ctxt != null) {

            String initialTrsfDebitAccountNumber = null;
            String initialTrsfChequeNumber = null;

            boolean hasDebitAccountNumberChanged = true;
            boolean hasChequeNumberChanged = true;

            if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctxt.getWizardMode())) {

                if (externalTransferContextBO instanceof ClearingTransferContextBO) {

                    ClearingTransferContextBO clearingCtxt = (ClearingTransferContextBO) externalTransferContextBO;

                    ClearingTransferBO clearingTrsf = clearingCtxt.getInitialTransfer();

                    initialTrsfDebitAccountNumber = clearingTrsf.getDebitAccount();
                    initialTrsfChequeNumber = clearingTrsf.getChequeNumber();

                } else if (externalTransferContextBO instanceof SepaTransferContextBO) {
                    SepaTransferContextBO sepaCtxt = (SepaTransferContextBO) externalTransferContextBO;

                    SepaTransferBO sepaTrsf = sepaCtxt.getInitialTransfer();

                    initialTrsfDebitAccountNumber = sepaTrsf.getDebitAccount();
                }

                hasDebitAccountNumberChanged = !Objects.equals(ctxt.getDebitAccountBO().getCode(), initialTrsfDebitAccountNumber);
                hasChequeNumberChanged = !Objects.equals(ctxt.getChequeNumber(), initialTrsfChequeNumber);
            }

            //calculate and set Xposoper
            //            GetOperOverdraftInfoResponse getOverdraftInfoResp =
            //                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null,
            //                                                                                ctxt.getDebitAccountBO().getCode(),
            //                                                                                ctxt.getCounterValueAmount(),
            //                                                                                ctxt.getUser().getUsername(),
            //                                                                                ctxt.getTransferType().toString(),
            //                                                                                ctxt.getUserLanguage());
            //            ctxt.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
            //ES 20180412 CBKSE-734 In modify mode, reference number should be sent
            GetOperOverdraftInfoResponse getOverdraftInfoResp = new GetOperOverdraftInfoResponse();
            if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctxt.getWizardMode())) {
                getOverdraftInfoResp =
                    paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(ctxt.getOperationReference(), ctxt.getDebitAccountBO().getCode(),
                                                                                    ctxt.getCounterValueAmount(), ctxt.getUser().getUsername(),
                                                                                    ctxt.getTransferType().toString(), ctxt.getUserLanguage());
            } else {
                getOverdraftInfoResp =
                    paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, ctxt.getDebitAccountBO().getCode(),
                                                                                    ctxt.getCounterValueAmount(), ctxt.getUser().getUsername(),
                                                                                    ctxt.getTransferType().toString(), ctxt.getUserLanguage());
            }
            ctxt.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);

            if (getOverdraftInfoResp != null && !getOverdraftInfoResp.hasError()) {
                ctxt.setOperationPositionCodeTmp(getOverdraftInfoResp.getOperPositionCode());

                if ("C".equals(getOverdraftInfoResp.getOperPositionCode()))
                    ctxt.getErrorResponse().addAcknowledgementText(translate("PROMPT_OVERDRAFT", ctxt.getUserLanguage()));
                else if ("B".equals(getOverdraftInfoResp.getOperPositionCode()))
                    ctxt.getErrorResponse().addAcknowledgementText(translate("PROMPT_NOT_OVERDRAFT", ctxt.getUserLanguage()));
            }

            //TODO: Note: LK: NUMCHQ code should be unified accrooss projects : TODO elio.
            if (ctxt.getChequeAttributes() == null)
                ctxt.setChequeAttributes(getChequeAttributes(ctxt.getTransferType().toString()));

            GetChequeNumAndRelativeRefAccessInfoResponse getCheckNumAndRelativeRefAccessInfoResponse =
                getChequeNumAndRelativeRefAccessInfo(ctxt.getTransferModeBO().getCode(), ctxt.getPeriodicityBO().getCode(),
                                                     ctxt.getDebitAccountBO().getLornosCode(), ctxt.getUser().getUsername(),
                                                     ctxt.getTransferType().toString(), ctxt.getUserLanguage());
            ctxt.getErrorResponse().handlePLSQLFunctionResponse(getCheckNumAndRelativeRefAccessInfoResponse);

            CheckAmountLedgerLimitResponse checkAmountLedgerLimitResponse =
                checkamountledgerlimit(ctxt.getCounterValueAmount(), ctxt.getDebitAccountBO().getLedgerCode(),
                                       ctxt.getDebitAccountBO().getCurrencyIsoCode(), ctxt.getDebitAccount().getAvailableBalance());
            ctxt.getErrorResponse().handlePLSQLFunctionResponse(checkAmountLedgerLimitResponse, ExternalTransferValidationEnum.DebitAmount);

            if (!getCheckNumAndRelativeRefAccessInfoResponse.hasError()) {

                ctxt.setChequeNumAndRefRelAccessInfo(getCheckNumAndRelativeRefAccessInfoResponse);
                //setting the access rights
                ctxt.setAccRelativeReference(getCheckNumAndRelativeRefAccessInfoResponse.getAccRelativeRef());
                ctxt.setAccChequeNumber(getCheckNumAndRelativeRefAccessInfoResponse.getAccChequeNumber());
                String isChequeNumberControlled = getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberControlled();
                String isChequeNumberEnabled = getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled();
                List<String> transferModeListForControlledChequeNum =
                    ctxt.getChequeAttributes() == null ? new ArrayList<String>() : ctxt.getChequeAttributes().getChequeTransferModeList();

                if ("Y".equalsIgnoreCase(isChequeNumberEnabled) &&
                    transferModeListForControlledChequeNum.contains(ctxt.getTransferModeBO().getCode()) &&
                    !"U".equals(ctxt.getPeriodicityBO().getCode()) && ctxt.getChequeNumber() != null) {

                    ctxt.getErrorResponse().addErrorText(ExternalTransferValidationEnum.ChequeNumber,
                                                         translate("MSG_INVALID_NUMCHQ_PERIOD", ctxt.getUserLanguage()));
                    return ctxt; //return the error before making the control on the validity of the cheque
                }

                if ("O".equalsIgnoreCase(isChequeNumberControlled) && "Y".equalsIgnoreCase(isChequeNumberEnabled) &&
                    transferModeListForControlledChequeNum.contains(ctxt.getTransferModeBO().getCode()) && ctxt.getChequeNumber() != null) {
                    if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctxt.getWizardMode()) && !hasDebitAccountNumberChanged &&
                          !hasChequeNumberChanged)) {
                        ControlChequeNumberResponse controlChequeNumberResponse =
                            controlChequeNumber(ctxt.getDebitAccountBO().getCode(), ctxt.getChequeNumber(), ctxt.getTransferType().getV2Applic());
                        ctxt.getErrorResponse().handlePLSQLFunctionResponse(controlChequeNumberResponse, ExternalTransferValidationEnum.ChequeNumber);
                    }
                }
            }
        }
        return ctxt;
    }

    //start Fucntions for the new approach1
    private ExternalTransferContextBO getExternalTransferContextBONextCommissions(ExternalTransferContextBO externalTransferContextBO) {
        ExternalTransferContextBO ctxt = externalTransferContextBO;
        String language = ctxt.getUserLanguage();
        setCoreBankingLanguage(language);

        //calculate and set Xposoper
        //        GetOperOverdraftInfoResponse getOverdraftInfoResp =
        //            paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, ctxt.getDebitAccountBO().getCode(),
        //                                                                            ctxt.getDebitNetAmount(),
        //                                                                            ctxt.getUser().getUsername(),
        //                                                                            ctxt.getTransferType().toString(),
        //                                                                            ctxt.getUserLanguage());
        //        ctxt.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
        //ES 20180412 CBKSE-734 In modify mode, reference number should be sent
        GetOperOverdraftInfoResponse getOverdraftInfoResp = new GetOperOverdraftInfoResponse();
        if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctxt.getWizardMode())) {
            getOverdraftInfoResp =
                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(ctxt.getOperationReference(), ctxt.getDebitAccountBO().getCode(),
                                                                                ctxt.getDebitNetAmount(), ctxt.getUser().getUsername(),
                                                                                ctxt.getTransferType().toString(), ctxt.getUserLanguage());
        } else {
            getOverdraftInfoResp =
                paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, ctxt.getDebitAccountBO().getCode(), ctxt.getDebitNetAmount(),
                                                                                ctxt.getUser().getUsername(), ctxt.getTransferType().toString(),
                                                                                ctxt.getUserLanguage());
        }
        ctxt.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);

        if (getOverdraftInfoResp != null) {
            if (!getOverdraftInfoResp.hasError()) {
                ctxt.setOperationPositionCode(getOverdraftInfoResp.getOperPositionCode());
                if (!Objects.equals(ctxt.getOperationPositionCode(), ctxt.getOperationPositionCodeTmp())) {
                    if ("C".equals(getOverdraftInfoResp.getOperPositionCode()))
                        ctxt.getErrorResponse().addAcknowledgementText(translate("PROMPT_OVERDRAFT", ctxt.getUserLanguage()));
                    else if ("B".equals(getOverdraftInfoResp.getOperPositionCode()))
                        ctxt.getErrorResponse().addAcknowledgementText(translate("PROMPT_NOT_OVERDRAFT", ctxt.getUserLanguage()));
                }
            } else //a warning message is added as it is not blocking if the calculation of operation position is not done.
                ctxt.getErrorResponse().addWarningText(getOverdraftInfoResp.getErrorText());
        }

        return ctxt;
    }

    public ClearingTransferContextBO getClearingTransferContextBONextCommissions(ClearingTransferContextBO ctx) {
        if (ctx != null) {
            ctx.getErrorResponse().clearAllResponses();
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONextCommissions(ctx);
        }
        return ctx;
    }

    public ClearingTransferContextBO getClearingTransferContextBONext1(ClearingTransferContextBO clearingTransferContextBO) {
        ClearingTransferContextBO ctx = clearingTransferContextBO;
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONext1A(ctx);

            // Control on key
            if ("REQ".equalsIgnoreCase(ctx.getAccAccountRib())) {
                CalculateAccountKeyResponse calculateAccountKeyResponse =
                    calculateAccountKey(ctx.getBankCodeBO().getCode(), ctx.getBankCounterCodeBO().getCode(), ctx.getAccount(),
                                        ctx.getUser().getUsername(), ctx.getTransferType().toString(), ctx.getUserLanguage());

                if (!ctx.getKey().equals(calculateAccountKeyResponse.getAccountKey())) {
                    ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.AccountKey, translate("MSG_KEY_VALUE", ctx.getUserLanguage()));
                    ctx.setKey(calculateAccountKeyResponse.getAccountKey());
                }
            }
            //control on RCOMPTE
            String fxy_MaxAccountControl = FxyUtils.getFxyXValue("ALL", "RIB", "X2", em);
            if ("OUI".equals(fxy_MaxAccountControl)) {
                Integer maxCharactersLength = ctx.getBankState().getAccountLength();
                if (maxCharactersLength != null) {
                    String cptprib = ctx.getBankState().getCptprib();
                    Integer nbrOfZeroes = cptprib != null ? cptprib.length() : 0;

                    Integer maxAccountLength = maxCharactersLength + nbrOfZeroes;
                    Integer temp = ctx.getAccount().length();

                    if (temp.compareTo(maxAccountLength) > 0) {
                        String message =
                            MessageFormat.format("{0} {1} {2}", translate("MSG_MAX_ACCOUNT_CHAR_CONTROL", ctx.getUserLanguage()), maxAccountLength,
                                                 translate("MSG_CHAR", ctx.getUserLanguage()));
                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.Account, message);
                    }
                }
            }
        }
        return ctx;
    }

    public ClearingTransferContextBO getClearingTransferContextBONext2(ClearingTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONext2A(ctx);
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONext2B(ctx);
        }
        return ctx;
    }

    public ClearingTransferContextBO getClearingTransferContextBONext3(ClearingTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONext3(ctx);
        }
        return ctx;
    }

    private ExternalTransferContextBO getExternalTransferContextBONext3(ExternalTransferContextBO externalTransferContextBO) {
        ExternalTransferContextBO ctx = externalTransferContextBO;
        setCoreBankingLanguage(ctx.getUserLanguage());
        if (ctx != null) {
            String initialTrsfDebitAccountNumber = null;
            String initialTrsfCreditAccountNumber = null;
            Date initialTrsfCreditValueDate = null;

            boolean hasDebitAccountNumberChanged = true;
            boolean hasCreditNumberChanged = true;
            boolean hasCreditValueDateChanged = true;

            if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                if (externalTransferContextBO instanceof ClearingTransferContextBO) {

                    ClearingTransferContextBO clearingCtxt = (ClearingTransferContextBO) externalTransferContextBO;
                    ClearingTransferBO clearingTrsf = clearingCtxt.getInitialTransfer();

                    initialTrsfDebitAccountNumber = clearingTrsf.getDebitAccount();
                    initialTrsfCreditAccountNumber = clearingTrsf.getCreditAccount();
                    initialTrsfCreditValueDate = clearingTrsf.getCreditValueDate();
                } else if (externalTransferContextBO instanceof SepaTransferContextBO) {
                    SepaTransferContextBO sepaCtxt = (SepaTransferContextBO) externalTransferContextBO;
                    SepaTransferBO sepaTrsf = sepaCtxt.getInitialTransfer();

                    initialTrsfDebitAccountNumber = sepaTrsf.getDebitAccount();
                    initialTrsfCreditAccountNumber = sepaTrsf.getCreditAccount();
                    initialTrsfCreditValueDate = sepaTrsf.getCreditValueDate();
                }

                hasDebitAccountNumberChanged = !Objects.equals(ctx.getDebitAccountBO().getCode(), initialTrsfDebitAccountNumber);
                hasCreditNumberChanged = !Objects.equals(ctx.getCorrespondentBO().getAccountNumber(), initialTrsfCreditAccountNumber);
                hasCreditValueDateChanged = !Objects.equals(ctx.getCreditValueDate(), initialTrsfCreditValueDate);
            }

            //Check if debit and credit account are the same
            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasDebitAccountNumberChanged &&
                  !hasCreditNumberChanged)) {
                if (ctx.getDebitAccount().getAccountNumber().equalsIgnoreCase(ctx.getCorrespondentBO().getAccountNumber())) {
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CorrespondentAccount,
                                                                  translate("MSG_SAME_DEBIT_CREDIT_ACCOUNT", ctx.getUserLanguage()));
                }
            }

            //retrieve data about the correspondent
            Account correspondentAccount = findAccountByCode(ctx.getCorrespondentBO().getAccountNumber());
            ctx.setCorrespondentAccount(correspondentAccount);

            String correspondentCountryIso =
                correspondentAccount != null && correspondentAccount.getClient() != null && correspondentAccount.getClient().getCountry() != null ?
                correspondentAccount.getClient().getCountry().getIsoCode2Chars() : null;
            String correspondentCurrencyIso = ctx.getCorrespondentBO() != null ? ctx.getCorrespondentBO().getCurrencyIsoCode() : null;

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && !hasCreditValueDateChanged)) {
                CheckIfDateIsOnWeekendResponse checkCreditValueDateWeekendResp =
                    getCheckIfDateIsOnWeekend(ctx.getCreditValueDate(), correspondentCountryIso, correspondentCurrencyIso);
                if (checkCreditValueDateWeekendResp != null && checkCreditValueDateWeekendResp.getIsOnWeekend()) {
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CreditValueDate,
                                                                  translate("PROMPT_HOLIDAY_1", ctx.getUserLanguage()) + " " +
                                                                  translate("CREDIT_VALUE_DATE", ctx.getUserLanguage()) + " " +
                                                                  translate("QUESTION_MARK", ctx.getUserLanguage()));
                }
            }
            // control MaxAllowedAmount
            if (ctx.getCorrespondentBO() != null && ctx.getCorrespondentBO().getMaxAllowedAmount() != null &&
                ctx.getAmount().compareTo(ctx.getCorrespondentBO().getMaxAllowedAmount()) > 0)
                ctx.getErrorResponse().addErrorText(translate("MSG_MAX_AMOUNT_CONTROL", ctx.getUserLanguage()));
        }
        return ctx;
    }

    public ClearingTransferContextBO getClearingTransferContextBONext4(ClearingTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (ClearingTransferContextBO) getExternalTransferContextBONext4(ctx);
        }
        return ctx;
    }

    private ExternalTransferContextBO getExternalTransferContextBONext4(ExternalTransferContextBO externalTransferContextBO) {
        return externalTransferContextBO;
    }

    public SepaTransferContextBO getSepaTransferContextBONext1(SepaTransferContextBO sepaTransferContextBO) {
        SepaTransferContextBO ctxt = sepaTransferContextBO;
        if (ctxt != null) {
            ctxt.getErrorResponse().clearAllResponses();
            ctxt = (SepaTransferContextBO) getExternalTransferContextBONext1A(sepaTransferContextBO);
            String iban = ctxt.getBeneficiaryIban();
            String benefBancBic = ctxt.getBeneficiaryBankBic().getBicCode();

            String beneficiaryCountryIsoCode = sepaTransferContextBO.getBeneficiaryBankCountryBO().getIsoCode2Chars();

            //Always convert iban to upper case
            if (sepaTransferContextBO.getBeneficiaryIban() != null)
                sepaTransferContextBO.setBeneficiaryIban(sepaTransferContextBO.getBeneficiaryIban().toUpperCase());

            CheckIbanValidResponse ibanValidResponse =
                getCheckIbanValid(beneficiaryCountryIsoCode, sepaTransferContextBO.getBeneficiaryIban(),
                                  sepaTransferContextBO.getUser().getUsername(), TransferTypeEnum.Sepa.toString(),
                                  sepaTransferContextBO.getUserLanguage());

            sepaTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(ibanValidResponse, ExternalTransferValidationEnum.Iban);

            if (!ibanValidResponse.hasError()) {
                CheckControlBbanBancBicResponse controlBbanBancBicResponse = getCheckControlBbanBancBic(iban, benefBancBic);
                sepaTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(controlBbanBancBicResponse, ExternalTransferValidationEnum.Iban);

                CheckValidateAccKeyWithFrIbanResponse validateAccKeyWithFrIbanResponse = getCheckValidateAccKeyWithFrIban(iban);
                sepaTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(validateAccKeyWithFrIbanResponse,
                                                                                     ExternalTransferValidationEnum.Iban);
            }

        }
        return ctxt;
    }

    public SepaTransferContextBO getSepaTransferContextBONext2(SepaTransferContextBO sepaTransferContextBO) {
        SepaTransferContextBO ctx = sepaTransferContextBO;
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();

            ctx = (SepaTransferContextBO) getExternalTransferContextBONext2A(sepaTransferContextBO);
            Account debitAccount = sepaTransferContextBO.getDebitAccount();
            BankState bankState = sepaTransferContextBO.getBankState();

            String orderingIban = null;
            if (bankState.getCountry().getIbanLength() !=
                null) { //instead of test_cee_iban
                CalculateRibKeyResponse calculateRibKeyResponse =
                    calculateRibKey(bankState.getCodbnq(), debitAccount.getBranchCode(), debitAccount.getAccountNumber());

                sepaTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(calculateRibKeyResponse);

                if (!calculateRibKeyResponse.hasError()) {
                    String ribKey = calculateRibKeyResponse.getRibKey();

                    CalculateIbanFromRibKeyResponse calculateIbanFromRibKeyResponse =
                        calculateIbanFromRibKey(bankState.getCodbnq(), debitAccount.getBranchCode(), debitAccount.getAccountNumber(), ribKey);

                    sepaTransferContextBO.getErrorResponse().handlePLSQLFunctionResponse(calculateIbanFromRibKeyResponse);

                    if (!calculateIbanFromRibKeyResponse.hasError())
                        orderingIban = calculateIbanFromRibKeyResponse.getIban();
                }
            } else
                orderingIban = debitAccount.getAccountNumber();

            if (sepaTransferContextBO.getBeneficiaryIban().equalsIgnoreCase(orderingIban))
                sepaTransferContextBO.getErrorResponse().addErrorText(ExternalTransferValidationEnum.DebitAccount,
                                                                      translate("MSG_SAME_IBAN_DC", ctx.getUserLanguage()));
            ctx = (SepaTransferContextBO) getExternalTransferContextBONext2B(ctx);
        }
        return ctx;
    }

    public SepaTransferContextBO getSepaTransferContextBONext3(SepaTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (SepaTransferContextBO) getExternalTransferContextBONext3(ctx);
        }
        return ctx;
    }

    public SepaTransferContextBO getSepaTransferContextBONext4(SepaTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (SepaTransferContextBO) getExternalTransferContextBONext4(ctx);
        }
        return ctx;
    }

    public SepaTransferContextBO getSepaTransferContextBONextCommissions(SepaTransferContextBO ctx) {
        if (ctx != null) {
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            ctx = (SepaTransferContextBO) getExternalTransferContextBONextCommissions(ctx);
        }
        return ctx;
    }

    private SaveSepaTransferResponse updateSepaTransfer(String operationReference, Date operationDate, String transferMode, String periodicityCode,
                                                        Date firstExecDate, Date maturityDate, String exchangeReference, String exchangeBuySellCode,
                                                        String fixedCurrencyIsoCode, String debitAccountNumber, Date debitValueDate,
                                                        String orderingOriginatorId, String orderingAddress1, String orderingAddress2,
                                                        String orderingPartyName, String motive, String adviceDescription, String orderingAdviceLng,
                                                        String orderingPartyIban, String creditAccountNumber, Date creditValueDate,
                                                        String benefBankBic, String benefName, String benefAddress, String benefOriginatorId,
                                                        String benefIban, BigDecimal CreditAmount, BigDecimal debitAmount, BigDecimal exchangeRate,
                                                        String StrongCcyIsoCode, String WeakCcyIsoCode, List<GlobalCommissionBO> commissionList,
                                                        String payRepEcoCode, Date cutoffDate, String printerCode, BigDecimal swiftVersionNumber,
                                                        String applic, String userName, String language, byte[] beforeImage) {

        List<CommissionStruct> commissionStructList = new ArrayList<CommissionStruct>();
        if (commissionList != null)
            for (int i = 0; i < commissionList.size(); i++)
                commissionStructList.add(commissionList.get(i).generateStruct());

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".ModifySepaTransfer");

        call.addNamedArgument("p_i_operationReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Operationdate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_Transfermode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Periodicity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Firstexecdate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_Finalmaturitydate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_Exchangeref", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Exchangebuysellcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_fixedccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Accountd", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Debitvaluedate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_Orderingoriginatorid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingaddress1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingaddress2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingpartyname", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Motive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Advicedesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingAdviceLng", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingPartyIban", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Accountc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Creditvaluedate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_Benefbankbic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Benefname", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Benefaddress", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Beneforiginatorid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Iban", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Creditamount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_Debitamount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_Exchangerate", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_strongccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_weakccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Commissionlist", CommissionStructUtil.commisionCollection());
        call.addNamedArgument("p_i_ecocode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_cutoffDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_printerCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE);
        call.addNamedArgument("p_i_beforeImg", JDBCTypes.BLOB_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_operationReference");
        query.addArgument("p_i_Operationdate");
        query.addArgument("p_i_Transfermode");
        query.addArgument("p_i_Periodicity");
        query.addArgument("p_i_Firstexecdate");
        query.addArgument("p_i_Finalmaturitydate");
        query.addArgument("p_i_Exchangeref");
        query.addArgument("p_i_Exchangebuysellcode");
        query.addArgument("p_i_fixedccy");
        query.addArgument("p_i_Accountd");
        query.addArgument("p_i_Debitvaluedate");
        query.addArgument("p_i_Orderingoriginatorid");
        query.addArgument("p_i_orderingaddress1");
        query.addArgument("p_i_orderingaddress2");
        query.addArgument("p_i_orderingpartyname");
        query.addArgument("p_i_Motive");
        query.addArgument("p_i_Advicedesc");
        query.addArgument("p_i_orderingAdviceLng");
        query.addArgument("p_i_orderingPartyIban");
        query.addArgument("p_i_Accountc");
        query.addArgument("p_i_Creditvaluedate");
        query.addArgument("p_i_Benefbankbic");
        query.addArgument("p_i_Benefname");
        query.addArgument("p_i_Benefaddress");
        query.addArgument("p_i_Beneforiginatorid");
        query.addArgument("p_i_Iban");
        query.addArgument("p_i_Creditamount");
        query.addArgument("p_i_Debitamount");
        query.addArgument("p_i_Exchangerate");
        query.addArgument("p_i_strongccy");
        query.addArgument("p_i_weakccy");
        query.addArgument("p_i_Commissionlist");
        query.addArgument("p_i_ecocode");
        query.addArgument("p_i_cutoffDate");
        query.addArgument("p_i_printerCode");
        query.addArgument("p_i_swiftVersionNumber");
        query.addArgument("p_i_beforeImg");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_user");
        query.addArgument("p_i_Lng");

        List queryArgs = new ArrayList();
        queryArgs.add(operationReference);
        queryArgs.add(operationDate);
        queryArgs.add(transferMode);
        queryArgs.add(periodicityCode);
        queryArgs.add(firstExecDate);
        queryArgs.add(maturityDate);
        queryArgs.add(exchangeReference);
        queryArgs.add(exchangeBuySellCode);
        queryArgs.add(fixedCurrencyIsoCode);
        queryArgs.add(debitAccountNumber);
        queryArgs.add(debitValueDate);
        queryArgs.add(orderingOriginatorId);
        queryArgs.add(orderingAddress1);
        queryArgs.add(orderingAddress2);
        queryArgs.add(orderingPartyName);
        queryArgs.add(motive);
        queryArgs.add(adviceDescription);
        queryArgs.add(orderingAdviceLng);
        queryArgs.add(orderingPartyIban);
        queryArgs.add(creditAccountNumber);
        queryArgs.add(creditValueDate);
        queryArgs.add(benefBankBic);
        queryArgs.add(benefName);
        queryArgs.add(benefAddress);
        queryArgs.add(benefOriginatorId);
        queryArgs.add(benefIban);
        queryArgs.add(CreditAmount);
        queryArgs.add(debitAmount);
        queryArgs.add(exchangeRate);
        queryArgs.add(StrongCcyIsoCode);
        queryArgs.add(WeakCcyIsoCode);
        queryArgs.add(commissionStructList);
        queryArgs.add(payRepEcoCode);
        queryArgs.add(cutoffDate);
        queryArgs.add(printerCode);
        queryArgs.add(swiftVersionNumber);
        queryArgs.add(beforeImage);
        queryArgs.add(applic);
        queryArgs.add(userName);
        queryArgs.add(language);

        ServerSession serverSession = ((JpaEntityManager) em.getDelegate()).getServerSession();
        serverSession.addDescriptor(CommissionStructUtil.commissionDescriptor());

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SaveSepaTransferResponse resp = new SaveSepaTransferResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setOperationreference(operationReference);
        return resp;
    }


    private SaveSepaTransferResponse createSepaTransfer(String operationReference, Date operationDate, String transferMode, String periodicityCode,
                                                        Date firstExecDate, Date maturityDate, String exchangeReference, String exchangeBuySellCode,
                                                        String fixedCcyIsoCode, String operationNature, String debitAccountNumber,
                                                        Date debitValueDate, String orderingOriginatorId, String orderingAddress1,
                                                        String orderingAddress2, String orderingPartyName, String motive, String adviceDescription,
                                                        String orderingAdviceLng, String relativeReference, String orderingPartyBankBic,
                                                        String orderingPartyIban, String creditAccountNumber, Date creditValueDate,
                                                        String benefBankBic, String benefName, String benefAddress, String benefOriginatorId,
                                                        String benefIban, String beneficiaryAdviceDesc, BigDecimal creditAmount,
                                                        BigDecimal debitAmount, BigDecimal exchangeRate, String strongCcyIsoCode,
                                                        String weakCcyIsoCode, List<GlobalCommissionBO> commissionList, String payRepEcoCode,
                                                        Date cutoffDate, String printerCode, String initialReference, BigDecimal swiftVersionNumber,
                                                        String applic, String userName, String language) {

        List<CommissionStruct> commissionStructList = new ArrayList<CommissionStruct>();
        if (commissionList != null)
            for (int i = 0; i < commissionList.size(); i++)
                commissionStructList.add(commissionList.get(i).generateStruct());

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".CreateSepaTransfer");

        call.addNamedArgument("p_i_operationdate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_transfermode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_periodicity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_firstexecdate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_finalmaturitydate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_exchangeref", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangebuysellcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_fixedccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_operationNature", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_forceOperationReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_accountd", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_debitvaluedate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_orderingoriginatorid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingaddress1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingaddress2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingpartyname", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_motive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_advicedesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingadvicelng", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_refrel", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingpartybankbic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_orderingPartyIban", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_accountc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_creditvaluedate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_benefbankbic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_benefname", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_benefaddress", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_beneforiginatorid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_iban", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_benefAdviceDesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_creditamount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_debitamount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_exchangerate", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_strongccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_weakccy", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_commissionlist", CommissionStructUtil.commisionCollection());
        call.addNamedArgument("p_i_ecocode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_cutoffDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_printerCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_initReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_operationreference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_operationdate");
        query.addArgument("p_i_transfermode");
        query.addArgument("p_i_periodicity");
        query.addArgument("p_i_firstexecdate");
        query.addArgument("p_i_finalmaturitydate");
        query.addArgument("p_i_exchangeref");
        query.addArgument("p_i_exchangebuysellcode");
        query.addArgument("p_i_fixedccy");
        query.addArgument("p_i_operationNature");
        query.addArgument("p_i_forceOperationReference");
        query.addArgument("p_i_accountd");
        query.addArgument("p_i_debitvaluedate");
        query.addArgument("p_i_orderingoriginatorid");
        query.addArgument("p_i_orderingaddress1");
        query.addArgument("p_i_orderingaddress2");
        query.addArgument("p_i_orderingpartyname");
        query.addArgument("p_i_motive");
        query.addArgument("p_i_advicedesc");
        query.addArgument("p_i_orderingadvicelng");
        query.addArgument("p_i_refrel");
        query.addArgument("p_i_orderingpartybankbic");
        query.addArgument("p_i_orderingPartyIban");
        query.addArgument("p_i_accountc");
        query.addArgument("p_i_creditvaluedate");
        query.addArgument("p_i_benefbankbic");
        query.addArgument("p_i_benefname");
        query.addArgument("p_i_benefaddress");
        query.addArgument("p_i_beneforiginatorid");
        query.addArgument("p_i_iban");
        query.addArgument("p_i_benefAdviceDesc");
        query.addArgument("p_i_creditamount");
        query.addArgument("p_i_debitamount");
        query.addArgument("p_i_exchangerate");
        query.addArgument("p_i_strongccy");
        query.addArgument("p_i_weakccy");
        query.addArgument("p_i_commissionlist");
        query.addArgument("p_i_ecocode");
        query.addArgument("p_i_cutoffDate");
        query.addArgument("p_i_printerCode");
        query.addArgument("p_i_initReference");
        query.addArgument("p_i_swiftVersionNumber");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_user");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(operationDate);
        queryArgs.add(transferMode);
        queryArgs.add(periodicityCode);
        queryArgs.add(firstExecDate);
        queryArgs.add(maturityDate);
        queryArgs.add(exchangeReference);
        queryArgs.add(exchangeBuySellCode);
        queryArgs.add(fixedCcyIsoCode);
        queryArgs.add(operationNature);
        queryArgs.add(operationReference);
        queryArgs.add(debitAccountNumber);
        queryArgs.add(debitValueDate);
        queryArgs.add(orderingOriginatorId);
        queryArgs.add(orderingAddress1);
        queryArgs.add(orderingAddress2);
        queryArgs.add(orderingPartyName);
        queryArgs.add(motive);
        queryArgs.add(adviceDescription);
        queryArgs.add(orderingAdviceLng);
        queryArgs.add(relativeReference);
        queryArgs.add(orderingPartyBankBic);
        queryArgs.add(orderingPartyIban);
        queryArgs.add(creditAccountNumber);
        queryArgs.add(creditValueDate);
        queryArgs.add(benefBankBic);
        queryArgs.add(benefName);
        queryArgs.add(benefAddress);
        queryArgs.add(benefOriginatorId);
        queryArgs.add(benefIban);
        queryArgs.add(beneficiaryAdviceDesc);
        queryArgs.add(creditAmount);
        queryArgs.add(debitAmount);
        queryArgs.add(exchangeRate);
        queryArgs.add(strongCcyIsoCode);
        queryArgs.add(weakCcyIsoCode);
        queryArgs.add(commissionStructList);
        queryArgs.add(payRepEcoCode);
        queryArgs.add(cutoffDate);
        queryArgs.add(printerCode);
        queryArgs.add(initialReference);
        queryArgs.add(swiftVersionNumber);
        queryArgs.add(applic);
        queryArgs.add(userName);
        queryArgs.add(language);

        ServerSession serverSession = ((JpaEntityManager) em.getDelegate()).getServerSession();
        serverSession.addDescriptor(CommissionStructUtil.commissionDescriptor());

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SaveSepaTransferResponse resp = new SaveSepaTransferResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setOperationreference((String) record.get("p_o_operationreference"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    public SaveSepaTransferResponse getSaveSepaTransfer(SepaTransferContextBO ctx, String printerCode) {
        return getSaveSepaTransfer(ctx, printerCode, null);
    }

    public SaveSepaTransferResponse getSaveSepaTransfer(SepaTransferContextBO ctx, String printerCode, BigDecimal swiftVersionNumber) {
        SaveSepaTransferResponse resp = new SaveSepaTransferResponse();

        if (ctx != null) {
            String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;
            /*String exchangeBuySellCode =
                ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getBuySellCode() : null;*/
            String buySellCode =
                PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_DEBIT_CODE.equalsIgnoreCase(ctx.getReferenceCurrencyCode()) ? "A" : "V";


            //Converting the step 2 String fields to uppercase
            ctx.setOrderingPartyName1(getUpperCaseSafely(ctx.getOrderingPartyName1()));
            ctx.setOrderingPartyAddress1(getUpperCaseSafely(ctx.getOrderingPartyAddress1()));
            ctx.setOrderingPartyAddress2(getUpperCaseSafely(ctx.getOrderingPartyAddress2()));
            ctx.setMotive(getUpperCaseSafely(getUpperCaseSafely(ctx.getMotive())));
            ctx.setClientRelativeReference(getUpperCaseSafely(ctx.getClientRelativeReference()));
            ctx.setAdviceDescription(getUpperCaseSafely(ctx.getAdviceDescription()));
            ctx.setOrderingPartyOriginatorId(getUpperCaseSafely(ctx.getOrderingPartyOriginatorId()));
            ctx.setOrderingPartyIban(getUpperCaseSafely(ctx.getOrderingPartyIban()));

            if (!PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
                if (PaymentsExternalTransferModelConstants.DUPLICATE.equals(ctx.getWizardMode())) //we empty the reference in duplicate mode as it might be filled in create mode when we are from received swift
                    ctx.setOperationReference(null);
                resp =
                    createSepaTransfer(ctx.getOperationReference(), ctx.getOperationDate(), ctx.getTransferModeBO().getCode(),
                                       ctx.getPeriodicityBO().getCode(), ctx.getFirstExecutionDate(), ctx.getFinalMaturityDate(), exchangeReference,
                                       buySellCode, ctx.getFixedCurrencyIsoCode(), ctx.getOperationNature(), ctx.getDebitAccountBO().getCode(),
                                       ctx.getDebitValueDate(), ctx.getOrderingPartyOriginatorId(), ctx.getOrderingPartyAddress1(),
                                       ctx.getOrderingPartyAddress2(), ctx.getOrderingPartyName1(), ctx.getMotive(), ctx.getAdviceDescription(),
                                       ctx.getDebitAdviceLanguageBO().getCode(), ctx.getClientRelativeReference(), ctx.getOrderingPartyBankBic(),
                                       ctx.getOrderingPartyIban(), ctx.getCorrespondentBO().getAccountNumber(), ctx.getCreditValueDate(),
                                       ctx.getBeneficiaryBankBic().getBicCode(), ctx.getBeneficiaryName(), ctx.getBeneficiaryAddress(),
                                       ctx.getBeneficiaryOriginatorId(), ctx.getBeneficiaryIban(), ctx.getBeneficiaryAdviceDesc(), ctx.getAmount(),
                                       ctx.getCounterValueAmount(), ctx.getExchangeRate(), ctx.getStrongCurrencyIsoCode(),
                                       ctx.getWeakCurrencyIsoCode(), ctx.getCommissionList(), "005", ctx.getCutoffDate(), printerCode,
                                       ctx.getInitialReference(), swiftVersionNumber, ctx.getTransferType().toString(), ctx.getUser().getUsername(),
                                       ctx.getUserLanguage());
            } else
                resp =
                    updateSepaTransfer(ctx.getOperationReference(), ctx.getOperationDate(), ctx.getTransferModeBO().getCode(),
                                       ctx.getPeriodicityBO().getCode(), ctx.getFirstExecutionDate(), ctx.getFinalMaturityDate(), exchangeReference,
                                       buySellCode, ctx.getFixedCurrencyIsoCode(), ctx.getDebitAccountBO().getCode(), ctx.getDebitValueDate(),
                                       ctx.getOrderingPartyOriginatorId(), ctx.getOrderingPartyAddress1(), ctx.getOrderingPartyAddress2(),
                                       ctx.getOrderingPartyName1(), ctx.getMotive(), ctx.getAdviceDescription(),
                                       ctx.getDebitAdviceLanguageBO().getCode(), ctx.getOrderingPartyIban(),
                                       ctx.getCorrespondentBO().getAccountNumber(), ctx.getCreditValueDate(),
                                       ctx.getBeneficiaryBankBic().getBicCode(), ctx.getBeneficiaryName(), ctx.getBeneficiaryAddress(),
                                       ctx.getBeneficiaryOriginatorId(), ctx.getBeneficiaryIban(), ctx.getAmount(), ctx.getCounterValueAmount(),
                                       ctx.getExchangeRate(), ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(), ctx.getCommissionList(),
                                       "005", ctx.getCutoffDate(), printerCode, swiftVersionNumber, ctx.getTransferType().toString(),
                                       ctx.getUser().getUsername(), ctx.getUserLanguage(), ctx.getInitialTransfer().getImage());
        }
        return resp;
    }


    private GetMaxAllowedOperDateResponse getMaxAllowedOperDate(String applic, Date currentDate) {

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_UTIL_PACKAGE_NAME + ".rch_max_datoper");

        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_datjour", JDBCTypes.DATE_TYPE);

        call.addNamedOutputArgument("p_o_maxdatoper", JDBCTypes.DATE_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_applic");
        query.addArgument("p_i_datjour");

        List queryArgs = new ArrayList();
        queryArgs.add(applic);
        queryArgs.add(currentDate);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetMaxAllowedOperDateResponse resp = new GetMaxAllowedOperDateResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setMaxAllowedDate((Date) record.get("p_o_maxOperDate"));
        return resp;
    }

    //End functions for the new appraoch
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public CRUDOperationResponseImpl getCreateSwiftTransfer(SwiftTransferContextBO ctx, String printerCode) {
        return getCreateSwiftTransfer(ctx, printerCode, null);

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public CRUDOperationResponseImpl getCreateSwiftTransfer(SwiftTransferContextBO ctx, String printerCode, BigDecimal swiftVersionNumber) {
        CRUDOperationResponseImpl createSwiftTransferResponse = new CRUDOperationResponseImpl();
        setCoreBankingLanguage(ctx.getUserLanguage());

        //start create for virest
        //SwiftTransferContextBO swiftTransferContextBO.getBankState().setNvirch(nvirch);
        SwiftTransfer swiftTransfer = new SwiftTransfer();

        String transferMode = ctx.getTransferMode() != null ? ctx.getTransferMode().getCode() : null;
        String userExpl = ctx.getLegacyUser().getExpl();

        String operationReference = ctx.getOperationReference();
        Date operationDate = ctx.getOperationDate();

        String debitAccount = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getAccountNumber() : null;
        String debitCurrency =
            ctx.getDebitAccount() != null && ctx.getDebitAccount().getCurrency() != null ? ctx.getDebitAccount().getCurrency().getIsoCode() : null;

        String correspondentCurrency = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getCurrency() : null;

        /* *****************************************
         * Operation Related Information
         * *****************************************/
        swiftTransfer.setOperationReference(operationReference);
        swiftTransfer.setInitiatorUserCode(userExpl);
        swiftTransfer.setUserServiceCode(ctx.getLegacyUser().getCserv());

        swiftTransfer.setOperationDate(operationDate);
        swiftTransfer.setDebitValueDate(ctx.getDebitValueDate());
        swiftTransfer.setCreditValueDate(ctx.getCreditValueDate());

        swiftTransfer.setNextExecutionDate(ctx.getFirstExecutionDate());

        if (ctx.getSwiftPeriodicity() != null)
            swiftTransfer.setPeriodicity(ctx.getSwiftPeriodicity().getCode());

        swiftTransfer.setChequeNumber(ctx.getChequeNumber());
        GetOperationAmountsResponse operationAmountsResponse =
            getOperationAmounts("N", transferMode, ctx.getExchangeRate(), ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(),
                                operationReference, debitAccount, debitCurrency, ctx.getCounterValueAmount(),
                                ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccount() : null, correspondentCurrency, ctx.getAmount(),
                                "O");

        if (!ctx.isPaymentReportingActiveFlag()) {
            String transferModeCode = ctx.getTransferModeBO() != null ? ctx.getTransferModeBO().getCode() : null;
            String beneficiaryBic = ctx.getBeneficiaryBic() != null ? ctx.getBeneficiaryBic().getBic() : null;
            String beneficiaryCountry =
                ctx.getBeneficiaryBic() != null && ctx.getBeneficiaryBic().getCountry() != null ?
                ctx.getBeneficiaryBic().getCountry().getIsoCode2Chars() : null;
            String beneficiaryBankBic = ctx.getBeneficiaryBankBic() != null ? ctx.getBeneficiaryBankBic().getBicCode() : null;
            String economicCode = ctx.getDebitAccountBO() != null ? ctx.getDebitAccountBO().getEconomicAgentCode() : null;
            String infoToBeneficiaryBank =
                !StringUtils.isEmpty(ctx.getInfoToBeneficiaryBankText()) ? ctx.getInfoToBeneficiaryBankText().trim() : null;
            String residence1 = ctx.getClient() != null ? ctx.getClient().getResid() : null;


            String debitCountryIso = null;
            if (ctx.getClient() != null && ctx.getClient().getCountry() != null) {
                debitCountryIso = ctx.getClient().getCountry().getIsoCode2Chars();
            }

            //decide on the residC
            String resid2 = null;
            String creditCountryIso = null;
            if (ctx.getBeneficiaryBic() != null) {
                resid2 =
                    (ctx.getBeneficiaryBic().getCountry() != null && ctx.getBeneficiaryBic().getCountry().getResidenceType() != null) ?
                    ctx.getBeneficiaryBic().getCountry().getResidenceType().getCode() : null;
                creditCountryIso = (ctx.getBeneficiaryBic().getCountry() != null) ? ctx.getBeneficiaryBic().getCountry().getIsoCode2Chars() : null;
            } else if (ctx.getBeneficiaryBankBic() != null) {
                resid2 = ctx.getBeneficiaryBankBic().getCountryResidCode();
                creditCountryIso = ctx.getBeneficiaryBankBic().getCountryIsoCode();
            }


            GetPaymentReportingDefaultValuesResponse getPaymentReportingDefaultValuesResponse =
                paymentReportingSessionEJB.getPaymentReportingDefaultValues(transferModeCode, "N", beneficiaryBic, beneficiaryCountry,
                                                                            beneficiaryBankBic, ctx.getBeneficiaryName(), economicCode,
                                                                            infoToBeneficiaryBank, debitCurrency,
                                                                            ctx.getBeneficiaryCorrespondentAccount(), ctx.getCounterValueAmount(),
                                                                            residence1, resid2, debitCountryIso, creditCountryIso,
                                                                            ctx.getUser().getUsercode(), TransferTypeEnum.Swift.getV2Applic(),
                                                                            ctx.getUserLanguage());


            if (getPaymentReportingDefaultValuesResponse != null && !getPaymentReportingDefaultValuesResponse.hasError()) {

                swiftTransfer.setPaymentReportCode(getPaymentReportingDefaultValuesResponse.getEconomicCode());
                swiftTransfer.setPaymentReportCountry(getPaymentReportingDefaultValuesResponse.getCountryIsoCode());
                swiftTransfer.setPaymentReportCrpCode(getPaymentReportingDefaultValuesResponse.getPaymentReportCode());
                swiftTransfer.setResidc(getPaymentReportingDefaultValuesResponse.getCreditResidenceTypeCode());
                swiftTransfer.setCreditAccountTypeCode(getPaymentReportingDefaultValuesResponse.getCreditAccountTypeCode());
            }
        } else {
            if (ctx.getPaymentReportingContextBO() != null) {
                String paymentReportingCrpCode =
                    ctx.getPaymentReportingContextBO().getPaymentReportingCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentReportingCodeBO().getCode() : null;
                String paymentReportingCountry =
                    ctx.getPaymentReportingContextBO().getPaymentCountryBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentCountryBO().getIsoCode2Chars() : null;
                String paymentEconomicCode =
                    ctx.getPaymentReportingContextBO().getPaymentEconomicCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentEconomicCodeBO().getCode() : null;
                String paymentResidence =
                    ctx.getPaymentReportingContextBO().getPaymentResidenceTypeCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentResidenceTypeCodeBO().getCode() : null;

                swiftTransfer.setPaymentReportCode(paymentEconomicCode);
                swiftTransfer.setPaymentReportCountry(paymentReportingCountry);
                swiftTransfer.setPaymentReportCrpCode(paymentReportingCrpCode);
                swiftTransfer.setResidc(paymentResidence);
                swiftTransfer.setCreditAccountTypeCode(ctx.getPaymentReportingContextBO().getPaymentAccountTypeCode());
            }
        }

        createSwiftTransferResponse.processPLSQLResponse(operationAmountsResponse);
        if (createSwiftTransferResponse.hasError())
            return createSwiftTransferResponse;

        BigDecimal debitNetAmount = operationAmountsResponse != null ? operationAmountsResponse.getDebitNetAmount() : null;
        BigDecimal creditNetAmount = operationAmountsResponse != null ? operationAmountsResponse.getCreditNetAmount() : null;

        if (debitNetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            createSwiftTransferResponse.setErrorText(translate("MSG_INVALID_DEBIT_NET_AMOUNT", ctx.getUserLanguage()));
            return createSwiftTransferResponse;
        }

        if (creditNetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            createSwiftTransferResponse.setErrorText(translate("MSG_INVALID_CREDIT_NET_AMOUNT", ctx.getUserLanguage()));
            return createSwiftTransferResponse;
        }

        String operationPositionCode = operationAmountsResponse != null ? operationAmountsResponse.getOperationPositionCode() : null;
        BigDecimal overdraftCounterValueAmount = operationAmountsResponse != null ? operationAmountsResponse.getOverdraftCounterValueAmount() : null;

        BigDecimal validAmount = null;

        if (debitCurrency.equalsIgnoreCase(ctx.getBankState().getDevref()))
            validAmount = debitNetAmount == null ? BigDecimal.ZERO : debitNetAmount;
        else {
            //get the current decimal position
            if (ctx.getDebitAccount() != null && ctx.getDebitAccount().getCurrency() != null &&
                ctx.getDebitAccount().getCurrency().getFract() != null && ctx.getDebitAccount().getCurrency().getCours() != null) {
                int currencyDecimalPosition = ctx.getDebitAccount().getCurrency().getFract().intValue();
                BigDecimal _debitNetAmount = debitNetAmount == null ? BigDecimal.ZERO : debitNetAmount;
                validAmount = ctx.getDebitAccount().getCurrency().getCours().multiply(_debitNetAmount);
                validAmount = validAmount.setScale(currencyDecimalPosition, RoundingMode.HALF_UP);
            }
        }

        swiftTransfer.setMntvalid(validAmount);
        swiftTransfer.setCutoffDate(ctx.getCutoffDate());

        if (ctx.getBdlZone() != null) {
            String correspondentAccountClient = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccountClient() : null;

            CheckIsBDLAmountInLimitResponse checkIsBDLAmountInLimitResponse =
                checkIsBDLAmountInLimit(correspondentAccountClient, correspondentCurrency, creditNetAmount, ctx.getUserLanguage());

            createSwiftTransferResponse.processPLSQLResponse(checkIsBDLAmountInLimitResponse);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;
        }

        //Retest amounts in ledger limit with commissions considered
        if (ctx.getDebitAccount() != null) {
            CheckIsAmountInLedgerLimitResponse checkIsAmountInLedgerLimitResponse =
                checkIsAmountInLedgerLimit(debitNetAmount, ctx.getDebitAccount().getAvailableBalance(), debitCurrency,
                                           ctx.getDebitAccount().getLedger() != null ? ctx.getDebitAccount().getLedger().getLedger() : null);
            createSwiftTransferResponse.processPLSQLResponse(checkIsAmountInLedgerLimitResponse);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;
        }

        String clientRelativeReference =
            !StringUtils.isEmpty(ctx.getClientRelativeReference()) ? ctx.getClientRelativeReference().toUpperCase().trim() : null;
        swiftTransfer.setRelativeReference(clientRelativeReference);

        if (ctx.getTransferMode() != null) {
            swiftTransfer.setOperationCode("VX" + transferMode);
            swiftTransfer.setTransferMode(transferMode);
        }

        String operationNature = ctx.getOperationNature();
        if (StringUtils.isEmpty(operationNature))
            operationNature = "NTRF";

        swiftTransfer.setOperationNatureCode(operationNature);

        swiftTransfer.setValidity(em.find(TransferValidity.class, "N"));
        swiftTransfer.setState("N");

        //TODO double check triggers on those sys variables as other apps can be inserting data to virest
        //Optimistic lock
        swiftTransfer.setSysCreatedBy(userExpl);
        swiftTransfer.setSysVersionNumber(new Long(1));
        swiftTransfer.setSysCreatedDate(new Date());

        String motive =
            !StringUtils.isEmpty(ctx.getMotive()) ? StringUtils.substringSafelyToLength(ctx.getMotive().toUpperCase().trim(), 0, 210) : null;
        swiftTransfer.setMotive1(motive);

        //swiftTransfer.setMotive2(StringUtils.substringSafelyToLength(ctx.getMotive() != null ?
        // ctx.getMotive().toUpperCase() : null, 70, 70));
        swiftTransfer.setMotive2(null);
        swiftTransfer.setVersionNumber(new Integer(1));
        swiftTransfer.setFinalMaturityDate(ctx.getFinalMaturityDate());
        swiftTransfer.setRenewFlag("N");
        swiftTransfer.setRequiredSignatureWeight(ctx.getLegacyUser().getPsign());
        swiftTransfer.setReachedSignatureNumber(Integer.valueOf(1));
        swiftTransfer.setRequiredSignatureNumber(Integer.valueOf(0));
        swiftTransfer.setTotsign(Integer.valueOf(0));
        swiftTransfer.setXpriorite("0");

        /* *****************************************
             * Ordering Party Information
             * *****************************************/
        if (ctx.getOrderingPartyNameAndCityResponse() != null)
            swiftTransfer.setOrderingPartyClientName(ctx.getOrderingPartyNameAndCityResponse().getOrderingPartyName());

        String orderingPartyName1 =
            !StringUtils.isEmpty(ctx.getOrderingPartyName1()) ?
            StringUtils.substringSafelyToLength(ctx.getOrderingPartyName1().toUpperCase().trim(), 0, 35) : null;
        swiftTransfer.setOrderingPartyName(orderingPartyName1);

        String orderingPartyName2 =
            !StringUtils.isEmpty(ctx.getOrderingPartyName2()) ?
            StringUtils.substringSafelyToLength(ctx.getOrderingPartyName2().toUpperCase().trim(), 0, 35) : null;
        swiftTransfer.setOrderingPartyName2(orderingPartyName2);

        String orderingPartyAddress1 =
            !StringUtils.isEmpty(ctx.getOrderingPartyAddress1()) ? ctx.getOrderingPartyAddress1().toUpperCase().trim() : null;
        swiftTransfer.setOrderingPartyAddress1(orderingPartyAddress1);

        String orderingPartyAddress2 =
            !StringUtils.isEmpty(ctx.getOrderingPartyAddress2()) ? ctx.getOrderingPartyAddress2().toUpperCase().trim() : null;
        swiftTransfer.setOrderingPartyAddress2(orderingPartyAddress2);

        swiftTransfer.setOrderingPartyInstitution(ctx.getOrderingInstitution() != null ? ctx.getOrderingInstitution().toUpperCase() : null);

        String orderingPartyReference =
            !StringUtils.isEmpty(ctx.getOrderingPartyReference()) ? ctx.getOrderingPartyReference().toUpperCase().trim() : "NEW";
        swiftTransfer.setOrderingPartyReference(orderingPartyReference);

        swiftTransfer.setOrderingPartyOnSwiftFlag("Y".equals(ctx.getOrderingPartyOnSwift()) ? "O" : "N");

        if (!StringUtils.isEmpty(ctx.getAccount50k()))
            swiftTransfer.setOrderingPartyIban(ctx.getAccount50k().toUpperCase().trim());

        if (ctx.getSwiftAdviceLanguage() != null)
            swiftTransfer.setOrderingPartyAdviceLanguageCode(ctx.getSwiftAdviceLanguage().getCode());

        swiftTransfer.setDebitAccount(ctx.getDebitAccount());
        if (ctx.getDebitAccount() != null) {
            swiftTransfer.setDebitLedgerCode(ctx.getDebitAccount().getLedger() != null ? ctx.getDebitAccount().getLedger().getLedger() : null);
            swiftTransfer.setDebitAccountTypeCode(ctx.getDebitAccount().getTyp());
            swiftTransfer.setLornosd(ctx.getDebitAccount().getLornos());
            swiftTransfer.setLornosd(ctx.getDebitAccount().getLornos());
        }

        if (ctx.getClient() != null)
            swiftTransfer.setResidd(ctx.getClient().getResid());

        String orderingPartyPostalCode =
            !StringUtils.isEmpty(ctx.getOrderingPartyPostalCode()) ? ctx.getOrderingPartyPostalCode().toUpperCase().trim() : null;
        swiftTransfer.setOrderingPartyPostalCode(orderingPartyPostalCode);

        swiftTransfer.setMt103PlusFlag(ctx.isIsMT103() ? "O" : "N");
        if(!ctx.isIsMT103() && ctx.getTransferMode() != null){
            FxyBO fxy = FxyUtils.getFxy("VIREST", "PRIORITYMSG", em, ctx.getTransferMode().getCode());
            if(fxy != null && "O".equals(fxy.getY2())){
                swiftTransfer.setPriorityMessage(fxy.getY1());
            }
        }
        if (ctx.getOrderingPartyNameAndCityResponse() != null)
            swiftTransfer.setOrderingPartyCityName(ctx.getOrderingPartyNameAndCityResponse().getCityName());

        if (ctx.getDebitAccount() != null && ctx.getDebitAccount().getProhibition() != null) {
            String prohibitionCode =
                ctx.getDebitAccount() != null && ctx.getDebitAccount().getProhibition() != null ? ctx.getDebitAccount().getProhibition().getCode() :
                null;

            GetAccountFinalProhibitionCodeResponse resp =
                paymentsTransferCommonModelSessionEJBLocal.getAccountFinalProhibitionCode("DEBIT", prohibitionCode);

            createSwiftTransferResponse.processPLSQLResponse(resp);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            if (resp != null)
                swiftTransfer.setDebitAccountProhibitionCode(resp.getProhibitionCode());
        }

        /* *****************************************
             * Advices Information
             * *****************************************/
        swiftTransfer.setMavis("N");
        if (ctx.getCorrespondent() != null && ctx.getCorrespondent().getAccountClientLanguage() != null)
            swiftTransfer.setCorrespondentAdviceLanguage(ctx.getCorrespondent().getAccountClientLanguage());
        swiftTransfer.setXlangb("N");
        swiftTransfer.setXlangq("N");
        swiftTransfer.setXlangcb("N");

        if (ctx.getOrderingPartyBic() != null)
            swiftTransfer.setOrderingPartyBic(findBankBicByBic(ctx.getOrderingPartyBic()));

        swiftTransfer.setOrderingPartyCommunicationCode(!StringUtils.isEmpty(ctx.getOrderingSendSwiftFlag()) ? ctx.getOrderingSendSwiftFlag() :
                                                        "C"); // S or C
        swiftTransfer.setOrderingPartyKeySwift("S".equalsIgnoreCase(ctx.getOrderingSendBicFlag()) ? ctx.getOrderingSendBicFlag() : null); // S or null

        swiftTransfer.setXlangci("N");

        GetDefaultAdviceGenerationTypeResponse getDefaultAdviceGenerationTypeResponse =
            getDefaultAdviceGenerationType(TransferTypeEnum.Swift.toString(), ctx.getLegacyUser().getCserv());
        createSwiftTransferResponse.processPLSQLResponse(getDefaultAdviceGenerationTypeResponse);
        if (createSwiftTransferResponse.hasError())
            return createSwiftTransferResponse;

        if (getDefaultAdviceGenerationTypeResponse != null)
            swiftTransfer.setAdviceGenerationTypeCode(getDefaultAdviceGenerationTypeResponse.getAdviceGenerationType());

        /* *****************************************
             * Beneficiary Information
             * *****************************************/
        if (ctx.getBeneficiaryBankBic() != null)
            swiftTransfer.setBeneficiaryBankBic(findBankBicByBic(ctx.getBeneficiaryBankBic().getBicCode()));

        //        String beneficiaryBankAccount =
        //            !StringUtils.isEmpty(ctx.getBeneficiaryBankAccount()) ?
        //            ctx.getBeneficiaryBankAccount().toUpperCase().trim() : null;
        //        swiftTransfer.setBeneficiaryBankAccount(beneficiaryBankAccount);

        String beneficiaryBankAccount = ctx.getBeneficiaryBankAccount();
        swiftTransfer.setBeneficiaryBankAccount(beneficiaryBankAccount);
        if (ctx.getBeneficiaryBankAccountSwiftAbr() != null) {
            beneficiaryBankAccount = ctx.getBeneficiaryBankAccountSwiftAbr().getAbbreviation();

            if (!StringUtils.isEmpty(ctx.getBeneficiaryBankAccount()))
                beneficiaryBankAccount += ctx.getBeneficiaryBankAccount().trim();

            swiftTransfer.setBeneficiaryBankAccount(beneficiaryBankAccount);
        }

        String beneficiaryBankBranch = !StringUtils.isEmpty(ctx.getBeneficiaryBankBranch()) ? ctx.getBeneficiaryBankBranch().trim() : null;
        swiftTransfer.setBeneficiaryBankBranch(beneficiaryBankBranch);

        if (!StringUtils.isEmpty(ctx.getBeneficiaryIban()))
            swiftTransfer.setBeneficiaryAccountIban(ctx.getBeneficiaryIban().toUpperCase().trim());

        String beneficiaryName = !StringUtils.isEmpty(ctx.getBeneficiaryName()) ? ctx.getBeneficiaryName().toUpperCase().trim() : null;
        swiftTransfer.setBeneficiaryName(beneficiaryName);

        String beneficiaryAddress =
            !StringUtils.isEmpty(ctx.getBeneficiaryAddress()) ?
            StringUtils.substringSafelyToLength(ctx.getBeneficiaryAddress().toUpperCase().trim(), 0, 70) : null;
        swiftTransfer.setBeneficiaryAddress(beneficiaryAddress);

        if (ctx.getBeneficiaryBic() != null) {
            swiftTransfer.setBeneficiaryCountryIsoCode(ctx.getBeneficiaryBic().getCountry() != null ?
                                                       ctx.getBeneficiaryBic().getCountry().getIsoCode2Chars() : null);
            swiftTransfer.setBeneficiaryCountryName(StringUtils.substringSafelyToLength(ctx.getBeneficiaryBic().getCityName(), 0, 25));
        }

        swiftTransfer.setXibanbe(ctx.getIbanControl());
        swiftTransfer.setIsodo(ctx.getResidenceCountryIso());

        String beneficiaryCorrespondentAccount =
            !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentAccount()) ? ctx.getBeneficiaryCorrespondentAccount().toUpperCase().trim() : null;
        swiftTransfer.setBeneficiaryCorrespondentAccount(beneficiaryCorrespondentAccount);


        if (ctx.getBeneficiaryBankBic() != null) {
            String beneficiaryBankCityName = StringUtils.substringSafelyToLength(ctx.getBeneficiaryBankBic().getCityName(), 0, 25); // KJ CBKS-1990
            String beneficiaryBankName =
                StringUtils.concatenate(" ".toCharArray(), ctx.getBeneficiaryBankBic().getBankName(), beneficiaryBankCityName);

            //TODO: check later business about a certain control on test siwft, based on param gxlangg, condition is never satisfied so i removed it for now
            swiftTransfer.setBeneficiaryBankCountryName(StringUtils.substringSafelyToLength(beneficiaryBankCityName, 0, 140));
            swiftTransfer.setBeneficiaryBankName(beneficiaryBankName != null ? beneficiaryBankName.toUpperCase() : null);
        } else {
            swiftTransfer.setBeneficiaryBankCountryName(StringUtils.substringSafelyToLength(ctx.getBeneficiaryBankCountryBO().getCountryName(), 0,
                                                                                            140)); // KJ CBKS-1990
            swiftTransfer.setBeneficiaryBankCountryIso(ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars());
            swiftTransfer.setBeneficiaryBankName(ctx.getBeneficiaryBankName() != null ? ctx.getBeneficiaryBankName().toUpperCase() : null);
        }

        swiftTransfer.setBeneficiaryBankKeySwift(ctx.getKeySwift());
        if (ctx.getBeneficiaryBankBic() != null) {
            String beneficiaryBankBicKeySwift = swiftTransfer.getBeneficiaryBankBic().getKeySwift();

            if (beneficiaryBankBicKeySwift != null && !beneficiaryBankBicKeySwift.equalsIgnoreCase("T") &&
                !beneficiaryBankBicKeySwift.equalsIgnoreCase("S")) {
                createSwiftTransferResponse.setErrorText(translate("MSG_INVALID_BENEF_BANK_BIC", ctx.getUserLanguage()));
                return createSwiftTransferResponse;
            }
            swiftTransfer.setBeneficiaryBankKeySwift(beneficiaryBankBicKeySwift);
        }

        String correspondentBankName =
            !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentBankName()) ? ctx.getBeneficiaryCorrespondentBankName().trim() : null;
        if (ctx.getBeneficiaryCorrespondentBic() != null) {
            String correspondentCityName =
                !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentBic().getCityName()) ?
                StringUtils.substringSafelyToLength(ctx.getBeneficiaryCorrespondentBic().getCityName(), 0, 25) : null;

            if (correspondentCityName != null)
                correspondentBankName =
                    !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentBic().getBankName()) ?
                    StringUtils.substringSafelyToLength(ctx.getBeneficiaryCorrespondentBic().getBankName() + " " + correspondentCityName, 0, 140) :
                    null;

            swiftTransfer.setBeneficiaryCorrespondentCountryName(correspondentCityName);
        }
        if (!StringUtils.isEmpty(correspondentBankName))
            swiftTransfer.setBeneficiaryCorrespondentBankName(correspondentBankName);

        String intermediaryBankName = !StringUtils.isEmpty(ctx.getIntermediaryBankName()) ? ctx.getIntermediaryBankName().trim() : null;
        if (ctx.getIntermediaryBic() != null)
            intermediaryBankName = ctx.getIntermediaryBic().getBankName() + " " + ctx.getIntermediaryBic().getCityName();

        if (!StringUtils.isEmpty(intermediaryBankName))
            swiftTransfer.setIntermediaryBankName(intermediaryBankName.toUpperCase().trim());

        if (ctx.getInfoToBeneficiaryBankSwiftAbr() != null) {
            String infoToBeneficiaryBank = ctx.getInfoToBeneficiaryBankSwiftAbr().getAbbreviation();

            if (!StringUtils.isEmpty(ctx.getInfoToBeneficiaryBankText()))
                infoToBeneficiaryBank += ctx.getInfoToBeneficiaryBankText().trim();

            swiftTransfer.setInfoToBeneficiaryBank(infoToBeneficiaryBank);
        }

        swiftTransfer.setBeneficiaryBic(ctx.getBeneficiaryBic());

        swiftTransfer.setBeneficiaryComCode(!StringUtils.isEmpty(ctx.getBenefSendSwiftFlag()) ? ctx.getBenefSendSwiftFlag() : "C"); // S or C
        swiftTransfer.setBeneficiaryKeySwift("S".equalsIgnoreCase(ctx.getBenefSendBicFlag()) ? ctx.getBenefSendBicFlag() : null); // S or null

        swiftTransfer.setBeneficiaryBankComCode(!StringUtils.isEmpty(ctx.getBenefBankSendSwiftFlag()) ? ctx.getBenefBankSendSwiftFlag() :
                                                "C"); // S or C
        swiftTransfer.setBeneficiaryBankKeySwift("S".equalsIgnoreCase(ctx.getBenefBankSendBicFlag()) ? ctx.getBenefBankSendBicFlag() :
                                                 null); // S or null

        if (ctx.getBeneficiaryCorrespondentBic() != null)
            swiftTransfer.setBeneficiaryCorrespondentBankBic(ctx.getBeneficiaryCorrespondentBic());

        swiftTransfer.setBeneficiaryCorrespondentComCode(!StringUtils.isEmpty(ctx.getBenefCorrespBankSendSwiftFlag()) ?
                                                         ctx.getBenefCorrespBankSendSwiftFlag() : "C"); // S or C
        swiftTransfer.setBeneficiaryCorrespondentKeySwift("S".equalsIgnoreCase(ctx.getBenefCorrespBankSendBicFlag()) ?
                                                          ctx.getBenefCorrespBankSendBicFlag() : null); // S or null

        swiftTransfer.setIntermediaryBankBic(ctx.getIntermediaryBic());

        swiftTransfer.setIntermediaryComCode(!StringUtils.isEmpty(ctx.getIntermediaryBankSendBicFlag()) ? ctx.getIntermediaryBankSendBicFlag() :
                                             "C"); // S or C

        /* *****************************************
             * Correspondent Information
             * *****************************************/
        String correspondentCountryName = null;
        if (ctx.getCorrespondent() != null) {
            swiftTransfer.setCreditAccount(findAccountByCode(ctx.getCorrespondent().getAccount()));
            swiftTransfer.setCreditLedgerCode(ctx.getCorrespondent().getAccountLedger());
            correspondentCountryName = StringUtils.substringSafelyToLength(ctx.getCorrespondent().getBicCityName(), 0, 25);
        }

        if (ctx.getCorrespondent() != null)
            swiftTransfer.setValueDateClass(ctx.getCorrespondent().getValueDateClass());

        //TODO normalization SWIFABR, to check with rania
        if (ctx.getBankOperationCode() != null)
            swiftTransfer.setBankOperationCode(ctx.getBankOperationCode().getAbbreviation() != null ?
                                               ctx.getBankOperationCode().getAbbreviation().substring(2, 6) : null);
        if (ctx.getInstructionCode() != null)
            swiftTransfer.setInstructionCode(ctx.getInstructionCode().getAbbreviation() != null ?
                                             ctx.getInstructionCode().getAbbreviation().substring(2, 6) : null);

        String information23 = !StringUtils.isEmpty(ctx.getInformation23()) ? ctx.getInformation23().trim() : null;
        swiftTransfer.setInformation23(information23);

        swiftTransfer.setOurAccount(ctx.getOurAccount());

        String correspondentName = null;
        if (ctx.getCorrespondent() != null)
            correspondentName = StringUtils.substringSafelyToLength(ctx.getCorrespondent().getBicBankName(), 0, 25);

        //correspondent bank country name (country3)
        if (ctx.getCorrespondent() == null) {
            GetOrderingPartyNameAndCityResponse getOrderingPartyNameAndCityResponse =
                getOrderingPartyNameAndCity(ctx.getCorrespondent().getAccountClient(), null, null);

            createSwiftTransferResponse.processPLSQLResponse(getOrderingPartyNameAndCityResponse);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            if (getOrderingPartyNameAndCityResponse != null) {
                correspondentName = getOrderingPartyNameAndCityResponse.getOrderingPartyName();
                correspondentCountryName = getOrderingPartyNameAndCityResponse.getCityName();
            }
        }

        swiftTransfer.setCorrespondentName(correspondentName);
        swiftTransfer.setCorrespodentCountryName(correspondentCountryName);

        if (ctx.getInfoToCorrespondent() != null) {
            String infoToCorrespondent = ctx.getInfoToCorrespondent().getAbbreviation();

            if (!StringUtils.isEmpty(ctx.getInfoToCorrespondentText()))
                infoToCorrespondent += ctx.getInfoToCorrespondentText().trim();

            swiftTransfer.setInfoToCorrespondent(infoToCorrespondent);
        }

        if (ctx.getCorrespondent() != null)
            swiftTransfer.setCorrespondentCountryIsoCode3Digits(ctx.getCorrespondent().getAccountClientCountry());

        if (ctx.getCorrespondent() != null) {
            //get the final prohibition Code
            String prohibitionCode = ctx.getCorrespondent().getAccountProhibition();

            GetAccountFinalProhibitionCodeResponse resp =
                paymentsTransferCommonModelSessionEJBLocal.getAccountFinalProhibitionCode("CREDIT", prohibitionCode);

            createSwiftTransferResponse.processPLSQLResponse(resp);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            if (resp != null)
                prohibitionCode = resp.getProhibitionCode();
            swiftTransfer.setCreditAccountProhibitionCode(prohibitionCode);
        }

        if (ctx.getCorrespondent() != null)
            swiftTransfer.setCorrespondentCountryIsoCode(ctx.getCorrespondent().getCountry());

        if (ctx.getBdlZone() != null)
            swiftTransfer.setBdlZone(ctx.getBdlZone().getCode());

        //get tribdl value
        if (ctx.getCorrespondent() != null) {
            GetBdlZoneInformationResponse getBdlZoneInformationResponse =
                getBdlZoneInformation(ctx.getCorrespondent().getAccountClient(), transferMode);
            if (getBdlZoneInformationResponse != null)
                swiftTransfer.setBdlZoneSortCode(getBdlZoneInformationResponse.getBdlZodeSortCode()); //TODO this might later be normalized: entity on XY
        }

        //ctlx fields
        swiftTransfer.setOurCorrespondentBic(ctx.getCorrespondantBic());
        swiftTransfer.setOurCorrespondentComCode(!StringUtils.isEmpty(ctx.getOurCorrespBankSendSwiftFlag()) ? ctx.getOurCorrespBankSendSwiftFlag() :
                                                 "C"); // S or C
        swiftTransfer.setOurCorrespondentKeySwift("S".equalsIgnoreCase(ctx.getOurCorrespBankSendBicFlag()) ? ctx.getOurCorrespBankSendBicFlag() :
                                                  null); // S or null

        /* *****************************************
             * Amounts & Exchange Rate Information
             * *****************************************/
        if (ctx.getDebitAccount() != null)
            swiftTransfer.setDebitCurrencyIsoCode(debitCurrency);
        swiftTransfer.setDebitAmount(ctx.getCounterValueAmount());
        swiftTransfer.setExchangeRate(ctx.getExchangeRate());
        swiftTransfer.setStrongCurrencyCode(ctx.getStrongCurrencyIsoCode());
        swiftTransfer.setWeakCurrencyCode(ctx.getWeakCurrencyIsoCode());
        if (ctx.getCorrespondent() != null)
            swiftTransfer.setCreditCurrencyIsoCode(correspondentCurrency);
        swiftTransfer.setCreditAmount(ctx.getAmount());

        if (ctx.getCorrespondent() != null) {
            GetAmountDescriptionResponse getAmountDescriptionResponse =
                getAmountDescription(correspondentCurrency, ctx.getSwiftAdviceLanguage() != null ? ctx.getSwiftAdviceLanguage().getCode() : null);

            createSwiftTransferResponse.processPLSQLResponse(getAmountDescriptionResponse);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            if (getAmountDescriptionResponse != null)
                swiftTransfer.setAmountDescription(getAmountDescriptionResponse.getDescription());
        }

        /* *****************************************
             * Commissions Information
             * *****************************************/
        swiftTransfer.setDebitNetAmount(debitNetAmount);
        swiftTransfer.setCreditNetAmount(creditNetAmount);
        swiftTransfer.setOperationPositionCode(operationPositionCode);
        swiftTransfer.setOverdraftCounterValueAmount(overdraftCounterValueAmount);

        swiftTransfer.setMnttotfr(BigDecimal.ZERO);
        if (ctx.getSwiftCharge() != null)
            swiftTransfer.setCharge(ctx.getSwiftCharge().getCode());
        swiftTransfer.setXcomchg("N");

        if (ctx.getDebitAccount() != null && ctx.getCorrespondent() != null) {
            GetCommissionDetailsResponse getCommissionDetailsResponse =
                getCommissionDetails(operationReference, debitCurrency, correspondentCurrency);

            createSwiftTransferResponse.processPLSQLResponse(getCommissionDetailsResponse);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            if (getCommissionDetailsResponse != null) {
                swiftTransfer.setCommissionAmount1(getCommissionDetailsResponse.getCommissionAmount1());
                swiftTransfer.setCommissionAmount2(getCommissionDetailsResponse.getCommissionAmount2());
                swiftTransfer.setPerceptionCode(getCommissionDetailsResponse.getPerception());
                swiftTransfer.setMandatoryCommissionFlag(getCommissionDetailsResponse.getMandatoryCommissionFlag());
            }
        }

        swiftTransfer.setSwiftCommissionTypeCode(ctx.getSwiftCommissionTypeCode());

        /* *****************************************
                     Some control on Chafic
         * *****************************************/
        String buySellCode =
            PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_DEBIT_CODE.equalsIgnoreCase(ctx.getReferenceCurrencyCode()) ? "A" : "V";
        ChaficPreInsertUpdateResponse chaficPreInsUpdResponse =
            chaficPreInsertUpdate(transferMode, ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(),
                                  ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null,
                                  /*ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getBuySellCode() :
                                  null,*/buySellCode, debitCurrency, correspondentCurrency, TransferTypeEnum.Swift.getV2Applic());

        createSwiftTransferResponse.processPLSQLResponse(chaficPreInsUpdResponse);
        if (createSwiftTransferResponse.hasError())
            return createSwiftTransferResponse;

        String insertChaficFlag = chaficPreInsUpdResponse != null ? chaficPreInsUpdResponse.getInsertChaficFlag() : null;
        String updateChaficFlag = chaficPreInsUpdResponse != null ? chaficPreInsUpdResponse.getUpdateChaficFlag() : null;
        String validChaficFlag = chaficPreInsUpdResponse != null ? chaficPreInsUpdResponse.getChaficValidFlag() : null;

        //If the exchange reference is emtpty, we invoke this fucntion to check if it should be automatically set to OperRef
        String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;

        if (exchangeReference == null) {
            GetExchangeRefFromOperRefResponse getExchangeRefFromOperRefResp =
                getExchangeRefFromOperRef(ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(), insertChaficFlag, operationReference);

            createSwiftTransferResponse.processPLSQLResponse(getExchangeRefFromOperRefResp);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;

            exchangeReference = getExchangeRefFromOperRefResp != null ? getExchangeRefFromOperRefResp.getExchangeReference() : exchangeReference;
        }

        swiftTransfer.setBeneficiaryBankCountryIso(ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars() :
                                                   null);
        swiftTransfer.setExchangeReference(exchangeReference);
        if (ctx.isCallingTfReceivedSwift()) {
            CheckSwiftVersionResponse checkswiftversionresp =
                getCheckSwiftVersionResponse(ctx.getInitialReference(), swiftVersionNumber, ctx.getLegacyUser().getUname(), "VIREST",
                                             ctx.getUserLanguage());
            createSwiftTransferResponse.processPLSQLResponse(checkswiftversionresp);
            if (createSwiftTransferResponse.hasError())
                return createSwiftTransferResponse;
        }

        try {
            userTransaction.begin();

            if (ctx.getChequeNumAndRefRelAccessInfo() != null) {
                if ((ctx.getChequeNumAndRefRelAccessInfo().getIsChequeNumberEnabled() != null &&
                     ctx.getChequeNumAndRefRelAccessInfo().getIsChequeNumberEnabled().equalsIgnoreCase("Y")) &&
                    (ctx.getChequeAttributes().getChequeTransferModeList() != null &&
                     ctx.getChequeAttributes().getChequeTransferModeList().contains(transferMode))) {

                    if (exchangeReference != null) {
                        ProcessPaidChequeResponse processPaidChequeResponse = processPaidCheque(debitAccount, ctx.getChequeNumber(), operationDate);
                        createSwiftTransferResponse.processPLSQLResponse(processPaidChequeResponse);
                    }
                }
                //TODO: those above functionsfunctions have no error handling in CGB. They should be reviewed
                /*
                if (createSwiftTransferResponse.hasError()){i
                    return createSwiftTransferResponse;
                } */
            }

            //Update state nvirch
            ctx.setBankState(incrementBankStateNvirtx());
            swiftTransfer.setRegisterReference("T".equalsIgnoreCase(transferMode) ? operationReference + "/" + ctx.getLegacyUser().getCserv() :
                                               "VXC" + ctx.getBankState().getNvirtx() + "/" + ctx.getLegacyUser().getCserv());

            // Swift Target
            if (ctx.isSwiftTarget()) {
                String priortgt = FxyUtils.getFxyValue("VIREST", "OPER2", "Y2", em, transferMode);

                // In case the value defined in Y2 is larger than 1 char
                if (!StringUtils.isEmpty(priortgt) && priortgt.length() > 1)
                    priortgt = priortgt.substring(0, 1);

                swiftTransfer.setPriortgt(priortgt);
            }
            em.persist(swiftTransfer);
            em.flush();

            //now run the PLSQL functions
            SetOperationWeightResponse setOperationWeightResponse = setOperationWeight(operationReference, TransferTypeEnum.Swift.toString());
            if (setOperationWeightResponse != null && setOperationWeightResponse.hasError())
                throw new SystemException(setOperationWeightResponse.getErrorText());

            ValidateWeightSignatureResponse validateWeightSignatureResponse =
                validateWeightSignature(operationReference, null, new Integer(1), "O", "O", printerCode, "dobatch", ctx.getInitialReference(),
                                        ctx.isCallingTfReceivedSwift() ? swiftVersionNumber : null, ctx.getLegacyUser().getUname(),
                                        TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());

            createSwiftTransferResponse.processPLSQLResponse(validateWeightSignatureResponse);
            if (createSwiftTransferResponse.hasError())
                throw new SystemException(createSwiftTransferResponse.getErrorText());

            //Get the new transfer entity to check if the REFDEB cheque should be paid
            SwiftTransfer newTrsf = findSwiftTransferByCode(operationReference);
            if ("V".equals(newTrsf.getValidity()) && !StringUtils.isEmpty(ctx.getOrderingPartyReference()) && ctx.getDebitAccount() != null) {
                ProcessPaidChequeResponse resp = processPaidCheque(debitAccount, ctx.getOrderingPartyReference().trim(), newTrsf.getOperationDate());

                createSwiftTransferResponse.processPLSQLResponse(resp);
                if (createSwiftTransferResponse.hasError())
                    throw new SystemException(resp.getErrorText());
            }

            //post insert script for chafic
            ChaficPostInsertResponse chaficPostInsertResponse =
                chaficPostInsert(operationDate, operationReference, exchangeReference, ctx.getCounterValueAmount(), ctx.getAmount(),
                                 ctx.getDebitValueDate(), ctx.getCreditValueDate(),
                                 ctx.getDebitAccount() != null && ctx.getDebitAccount().getClient() != null ?
                                 ctx.getDebitAccount().getClient().getClient() : null,
                                 // debit client id
                                 /*ctx.getCorrespondentAccount() != null &&
                                 ctx.getCorrespondentAccount().getClient() != null ?
                                 ctx.getCorrespondentAccount().getClient().getClient() : null,*/


                // set both clients to debit client QDM-671


                ctx.getDebitAccount() != null && ctx.getDebitAccount().getClient() != null ? ctx.getDebitAccount().getClient().getClient() : null,
                // credit client id
                ctx.getDebitAccount() != null && ctx.getDebitAccount().getClient() != null ? ctx.getDebitAccount().getClient().getName() : null,
                // debit client name
                //ctx.getCorrespondentAccount() != null &&
                /*ctx.getCorrespondentAccount().getClient() != null ?
                                 ctx.getCorrespondentAccount().getClient().getName() : null,*/


                // set both clients to debit client QDM-671


                ctx.getDebitAccount() != null && ctx.getDebitAccount().getClient() != null ? ctx.getDebitAccount().getClient().getName() : null,
                // credit client name
                debitAccount,
                // debit account
                ctx.getCorrespondentAccount() != null ? ctx.getCorrespondentAccount().getAccountNumber() : null,
                // credit account
                ctx.getDebitAccount() != null ? ctx.getDebitAccount().getCurrencyCode() : null,
                // debit currency
                correspondentCurrency, // credit currency
                ctx.getFixedCurrencyIsoCode(), ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(), ctx.getExchangeRate(),
                /*ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getBuySellCode() :
                                 null,*/buySellCode, null, // advice description
                userExpl, newTrsf.getValidity().getCode(), validChaficFlag, insertChaficFlag, updateChaficFlag, "1",
                TransferTypeEnum.Swift.getV2Applic());

            createSwiftTransferResponse.processPLSQLResponse(chaficPostInsertResponse);
            if (createSwiftTransferResponse.hasError())
                throw new SystemException(createSwiftTransferResponse.getErrorText());

            userTransaction.commit();

        } catch (SystemException se) {
            createSwiftTransferResponse.handleException(se, userTransaction);
        } catch (SecurityException se) {
            createSwiftTransferResponse.handleException(se, userTransaction);
        } catch (HeuristicRollbackException hre) {
            createSwiftTransferResponse.handleException(hre, userTransaction);
        } catch (HeuristicMixedException hme) {
            createSwiftTransferResponse.handleException(hme, userTransaction);
        } catch (RollbackException re) {
            createSwiftTransferResponse.handleException(re, userTransaction);
        } catch (NotSupportedException nse) {
            createSwiftTransferResponse.handleException(nse, userTransaction);
        } catch (IllegalStateException ise) {
            createSwiftTransferResponse.handleException(ise, userTransaction);
        } catch (RuntimeException rte) {
            createSwiftTransferResponse.handleException(rte, userTransaction);
        } finally {
            if (operationReference != null)
                createSwiftTransferResponse.setEntityObject(findSwiftTransferByCode(operationReference));
        }
        return createSwiftTransferResponse;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    private BankState incrementBankStateNvirtx() {
        String sqlString = "Update STATE set nvirtx = nvirtx + 1";
        Query query = em.createNativeQuery(sqlString);
        query.executeUpdate();
        return getBankState();
    }

    public GetExternalTransferTypeResponse checkTransferType(ExternalTransferContextBO ctx) {
        String benefBankBic = null;
        String benefBankCountry = null;

        if (ctx != null) {
            benefBankBic = ctx.getBeneficiaryBankBic() != null ? ctx.getBeneficiaryBankBic().getBicCode() : null;
            benefBankCountry = ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars() : null;
        }
        return checkTransferType(benefBankBic, benefBankCountry);
    }

    public GetExternalTransferTypeResponse checkTransferType(String benefBankBic, String benefBankCountry) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_BenefBankBic", JDBCTypes.VARCHAR_TYPE, benefBankBic));
        params.add(new PlsqlInputParameter("p_i_BenefBankCountry", JDBCTypes.VARCHAR_TYPE, benefBankCountry));
        params.add(new PlsqlOutputParameter("p_o_TransferTypeCode", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_UI_UTIL_PACKAGE_NAME, "getExternalTransferType", params, em);

        GetExternalTransferTypeResponse resp = new GetExternalTransferTypeResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        resp.setTransferTypeCode((String) returnParams.get("p_o_TransferTypeCode"));
        return resp;
    }

    private Date getMaxAllowedTransferDate(String referenceCurrencyIsoCode, Date oldDate, String applic) {

        Date maxAllowedTransferDate = null;
        String sql =
            "SELECT y7 FROM fx5y8 WHERE tname='ALL' AND model='DATVALDEF' AND x1 = Decode( ? ,'VIRSEPA','VIRSEP','VIRCHAC','VIRCHA', ?) AND x2 = '*' ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, applic);
        query.setParameter(2, applic);
        List<String> resultList = query.getResultList();
        String maxDateFormula = null;
        if (resultList != null && resultList.size() > 0)
            maxDateFormula = resultList.get(0);

        //TODO changes should be added in case of VIRCHAC (later)
        if (maxDateFormula != null) {
            JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
            Session session = jpaEntityManager.getActiveSession();

            PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
            call.setProcedureName("a.datcalc");

            call.addNamedArgument("p_i_devise", JDBCTypes.VARCHAR_TYPE);
            call.addNamedArgument("p_i_date_old", JDBCTypes.DATE_TYPE);
            call.addNamedArgument("p_i_datcod", JDBCTypes.VARCHAR_TYPE);

            call.setResult(JDBCTypes.DATE_TYPE);

            DataReadQuery dataReadQuery = new DataReadQuery();
            dataReadQuery.setCall(call);
            dataReadQuery.addArgument("p_i_devise");
            dataReadQuery.addArgument("p_i_date_old");
            dataReadQuery.addArgument("p_i_datcod");

            List queryArgs = new ArrayList();
            queryArgs.add(referenceCurrencyIsoCode);
            queryArgs.add(oldDate);
            queryArgs.add(maxDateFormula);

            List results = (List) session.executeQuery(dataReadQuery, queryArgs);
            DatabaseRecord record = (DatabaseRecord) results.get(0);

            CalculateTransferDateResponse resp = new CalculateTransferDateResponse();
            resp.setCalculatedDate((Date) record.get("RESULT"));
            return resp.getCalculatedDate();
        }
        return maxAllowedTransferDate;
    }

    /** wizard virest step 2 **/
    public List<SwiftAdviceLanguage> getSwiftAdviceLanguageFindAll() {
        String sql = "SELECT code, description FROM v_xy_virest_avis";

        List<SwiftAdviceLanguage> swiftAdviceLanguageList =
            QueryUtils.executeNativeQuery(sql, SwiftAdviceLanguage.class, new ArrayList<Object>(), em);
        return swiftAdviceLanguageList;
    }

    private SwiftAdviceLanguage getSwiftAdviceLanguage(String swiftAdviveLanguageCode) {
        List<SwiftAdviceLanguage> swiftAdviceLanguageList = getSwiftAdviceLanguageFindAll();
        if (swiftAdviceLanguageList != null && !swiftAdviceLanguageList.isEmpty())
            for (SwiftAdviceLanguage swiftAdviceLanguage : swiftAdviceLanguageList)
                if (swiftAdviceLanguage != null && swiftAdviceLanguage.getCode() != null &&
                    swiftAdviceLanguage.getCode().equals(swiftAdviveLanguageCode))
                    return swiftAdviceLanguage;
        return null;
    }

    public List<SwiftTransferMode> getSwiftTransferModeSearch(String treasuryTransferFlag, String username) {
        return getSwiftTransferModeFilteredSearch(treasuryTransferFlag, null, username, null, null);
    }

    public List<SwiftTransferMode> getSwiftTransferModeFilteredSearch(String treasuryTransferFlag, Boolean RTGSFlag, String username, String applic,
                                                                      String language) {
        return getSwiftTransferModeFilteredSearchCallingTf(treasuryTransferFlag, RTGSFlag, null, username, applic, language);
    }

    public List<SwiftTransferMode> getSwiftTransferModeFilteredSearchCallingTf(String treasuryTransferFlag, Boolean RTGSFlag, String callingTf,
                                                                               String username, String applic, String language) {
        User user = username != null ? getUser(username) : null;

        String userAuth = null;
        String userUnauth = null;
        if (user != null) {
            GetUserOperationAuthorizationResponse getUserOperAuthResp =
                paymentsTransferCommonModelSessionEJBLocal.getUserOperationAuthorization(TransferTypeEnum.Swift.toString(), user.getServiceCode(),
                                                                                         user.getUsercode());
            userAuth = getUserOperAuthResp != null ? getUserOperAuthResp.getAuthorizedOperationList() : "";
            userUnauth = getUserOperAuthResp != null ? getUserOperAuthResp.getUnauthorizedOperationList() : "";
        }

        String sql =
            "SELECT code, description, correspondentCommunication, ledgerCode, debitLedgerGroup, " +
            "sameCurrencyFlag, counter, bocio23e, xtarget FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getSwiftTransferModeList(?, ?, ?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(treasuryTransferFlag);
        params.add(userAuth);
        params.add(userUnauth);
        params.add(callingTf);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SwiftTransferMode> swiftTransferModeList = QueryUtils.executeNativeQuery(sql, SwiftTransferMode.class, params, em);

        //If RTGS is selected, then return list of tarnsfer modes not containing the RTGS modes
        if (RTGSFlag != null && !RTGSFlag && swiftTransferModeList != null && !swiftTransferModeList.isEmpty()) {
            List<RTGSTransferTypeBO> rtgsModeList = getRTGSTransferModeBOList(username, applic, language);
            swiftTransferModeList =
                new ArrayList<SwiftTransferMode>(Collections2.filter(swiftTransferModeList, new SwiftTransferModeRTGSPredicate(rtgsModeList)));
            return swiftTransferModeList;
        }

        return swiftTransferModeList;
    }

    public List<BankBic> getCorrespBankBic(Correspondent corresp) {
        List<BankBic> bicList = new ArrayList<BankBic>();
        if (corresp != null && corresp.getBic() != null)
            bicList.add(findBankBicByBic(corresp.getBic()));
        return bicList;
    }

    @Override
    public List<Account> getTransferAccountListSearch(String clientId, String directionCode, String ledgerGroupCode, String currencyIsoCode,
                                                      String userActivityCodeList, String loggedInUserClientId, Boolean filterUserOwnAccounts) {
        //LK TODO: when applicAllowedAction is filled, we might removed the left join
        /*
             * LK: Following discussion with Fouad, the JPQL will be modified in order to use the
             * CPT.OPLIST field instead of the AccountAllowedActionList, and this until the accounts
             * module is fully normalied.
             String jpql =
            "SELECT a  FROM Account a " +
            "   LEFT JOIN a.accountAllowedActionList acaal LEFT JOIN a.applic.applicAllowedActionList apaal JOIN a.ledger.ledgerGroupList lgl " + //LEFT JOIN UserActivity liua  " +
            "   WHERE a.client.client = :clientId " +
            "       AND a.currency.isoCode = COALESCE(:currencyCode, a.currency.isoCode) " +
            "       AND a.closingDate IS NULL " +
            "       AND (    (a.prohibition.code NOT IN ('AI','DI','OI') AND upper(:direction) = 'D' ) " +
            "           OR   (a.prohibition.code NOT IN ('AI','CI','OI') AND upper(:direction) = 'C' ) " +
            "           OR    ( a.prohibition is NULL  AND (  ( acaal.allowedAction.opcode = 'DC' AND a.accountAllowedActionList IS NOT EMPTY )   " +
            "                                                OR (apaal.allowedAction.opcode = 'DC' AND a.accountAllowedActionList IS EMPTY ) )" +
            "                  ) ) " +
            "       AND lgl.ledgerCategoryDesc = 'FORM' " +
            "       AND lgl.ledgerGroupCode = COALESCE(  :ledgerGroupCode , lgl.ledgerGroupCode ) ";
            */
        String jpql =
            "SELECT  DISTINCT a FROM Account a JOIN a.ledger.ledgerGroupList lgl " + //LEFT JOIN UserActivity liua  " +
            "   WHERE a.client.client = :clientId " + "       AND a.currency.isoCode = COALESCE(:currencyCode, a.currency.isoCode) " +
            "       AND a.closingDate IS NULL " + "       AND (    (a.prohibition.code NOT IN ('AI','DI','OI') AND upper(:direction) = 'D' ) " +
            "           OR   (a.prohibition.code NOT IN ('AI','CI','OI') AND upper(:direction) = 'C' ) " +
            "           OR    ( a.prohibition is NULL  AND (  ( LOCATE('DC', a.oplist) <> 0 AND a.oplist IS NOT NULL )   " +
            "                                                OR   (   LOCATE('DC', a.applic.allowedAction) <> 0  AND a.oplist IS NULL ) )" +
            "                  ) ) " + "       AND lgl.ledgerCategoryDesc = 'FORM' " +
            "       AND lgl.ledgerGroupCode = COALESCE(  :ledgerGroupCode , lgl.ledgerGroupCode ) ";

        if (filterUserOwnAccounts)
            jpql += " AND (a.client.client != :loggedInUserClientId OR :loggedInUserClientId IS NULL)";

        if (userActivityCodeList != null)
            jpql += " AND (  a.ledger.activity.code IN :userActivityCodeList OR  '*' IN  :userActivityCodeList ) ";

        jpql += " order by a.currency.isoCode, a.accountNumber";

        Query query = em.createQuery(jpql, Account.class);
        query.setParameter("clientId", clientId);
        query.setParameter("direction", directionCode);
        query.setParameter("currencyCode", currencyIsoCode);
        query.setParameter("ledgerGroupCode", ledgerGroupCode);

        if (userActivityCodeList != null)
            query.setParameter("userActivityCodeList", new ArrayList<String>(Arrays.asList(userActivityCodeList.split(","))));

        if (filterUserOwnAccounts)
            query.setParameter("loggedInUserClientId", loggedInUserClientId);

        Session session = em.unwrap(JpaEntityManager.class).getActiveSession();
        DatabaseQuery databaseQuery = ((EJBQueryImpl) query).getDatabaseQuery();
        databaseQuery.prepareCall(session, new DatabaseRecord());
        String sqlString = databaseQuery.getSQLString();
        System.out.println("THE SQL STRING IS ");
        System.out.println(sqlString);

        List<Account> accountList = query.getResultList();
        return accountList;
    }

    public List<Correspondent> getTransferCorrespondentFilteredList(String currencyIsoCodeFilter, String bicCodeFilter, String countryIsoCodeFilter,
                                                                    String directionCode, String loggedInUserClientId, Boolean filterUserOwnAccounts,
                                                                    String activityFilter, String operationMode, Boolean RTGLFlag,
                                                                    Boolean isSwiftTarget, String username, String applic, String language) {
        List<Correspondent> selectedList = new ArrayList<Correspondent>();
        List<Correspondent> correspondentList =
            getTransferCorrespondentList(currencyIsoCodeFilter, bicCodeFilter, countryIsoCodeFilter, directionCode, loggedInUserClientId,
                                         filterUserOwnAccounts, activityFilter, operationMode, isSwiftTarget, username, applic, language);
        if (RTGLFlag != null && RTGLFlag) {
            if (correspondentList != null && !correspondentList.isEmpty()) {
                List<AccountCorrespFXYFilteredBicBO> accountCorrespFilteredList = getAccountCorrespFXYFilteredBicBOList(username, applic, language);
                if (accountCorrespFilteredList != null && !accountCorrespFilteredList.isEmpty()) {
                    List<String> bicList = new ArrayList<String>();
                    for (AccountCorrespFXYFilteredBicBO accCorrespBO : accountCorrespFilteredList)
                        bicList.add(accCorrespBO.getCode());

                    for (Correspondent correspBO : correspondentList)
                        if (correspBO.getBic() != null && bicList.contains(correspBO.getBic()))
                            selectedList.add(correspBO);
                }
            }
        } else
            selectedList = correspondentList;

        return selectedList;
    }

    public List<Correspondent> getTransferCorrespondentList(String currencyIsoCodeFilter, String bicCodeFilter, String countryIsoCodeFilter,
                                                            String directionCode, String loggedInUserClientId, Boolean filterUserOwnAccounts,
                                                            String activityFilter, String operationMode, Boolean isSwiftTarget, String username,
                                                            String applic, String language) {
        String sql =
            "SELECT id, applic, bocio, bic, bicNom, bicUnpub, bicVille, xtypcion, compte, compteNom, compteTyp, " +
            "compteLornos, compteApplic, compteNcg, compteNcgActivite, compteCoddci, compteOplist, compteClient, " +
            "compteClientResid, compteClientLangue, compteClientPays, compteClientPaysNom, debcre, devise, iso, " +
            "paysNom, cles, datmaj, expl, mntseuil, seuil, cptcor, ordre, sys_created_by, sys_created_date, " +
            "sys_updated_by, sys_updated_date, sys_version_number, clasdatval, xswiftTargetDft, compteNcgDisc FROM TABLE(" +
            PLSQL_GLOBAL_PACKAGE_NAME + ".getTransferCorrespondentList(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)) order by ordre";

        List<Object> params = new ArrayList<Object>();
        params.add(bicCodeFilter);
        params.add(operationMode);
        params.add(currencyIsoCodeFilter);
        params.add(activityFilter);
        params.add(countryIsoCodeFilter);
        params.add(directionCode);
        params.add(loggedInUserClientId);
        params.add(filterUserOwnAccounts != null && filterUserOwnAccounts ? "Y" : "N");
        params.add(isSwiftTarget != null && isSwiftTarget ? "O" : "N");
        params.add(username);
        params.add(applic);
        params.add(language);

        List<Correspondent> correspondentlist = QueryUtils.executeNativeQuery(sql, Correspondent.class, params, em);
        return correspondentlist;
    }

    public CheckIbanValidResponse getCheckIbanValid(String countryIsoCode, String iban, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getCheckIbanValid(countryIsoCode, iban, username, applic, language);
    }

    public CheckValidateAccKeyWithFrIbanResponse getCheckValidateAccKeyWithFrIban(String iban) {
        return paymentsTransferCommonModelSessionEJBLocal.getCheckValidateAccKeyWithFrIban(iban);
    }

    public CheckControlBbanBancBicResponse getCheckControlBbanBancBic(String iban, String benefBancBic) {
        return paymentsTransferCommonModelSessionEJBLocal.getCheckControlBbanBancBic(iban, benefBancBic);
    }

    public List<Currency> getTransferCurrencyList(String currencyIsoCodeFilter, String bicCodeFilter, String countryIsoCodeFilter,
                                                  String directionCode, String loggedInUserClientId, Boolean filterUserOwnAccounts,
                                                  String activityFilter, String operationMode, Boolean isSwiftTarget, String username, String applic,
                                                  String language) {

        String currencyOperator = FxyUtils.getFxyValue("VIREST", "OPER", "Y6", em, "VX" + operationMode);
        boolean equalCurrencies = !StringUtils.isEmpty(currencyOperator) && "=".equals(currencyOperator.trim());
        boolean differentCurrencies = !StringUtils.isEmpty(currencyOperator) && "<>".equals(currencyOperator.trim());

        List<Correspondent> correspondentList =
            getTransferCorrespondentList(equalCurrencies ? currencyIsoCodeFilter : null, bicCodeFilter, countryIsoCodeFilter, directionCode,
                                         loggedInUserClientId, filterUserOwnAccounts, activityFilter, operationMode, isSwiftTarget, username, applic,
                                         language);

        List<Currency> transferCurrencyList = new ArrayList<Currency>();
        if (correspondentList != null) {
            Currency currency = null;
            for (Correspondent c : correspondentList) {
                currency = findCurrencyByCode(c.getCurrency());

                if (currency != null && !transferCurrencyList.contains(currency) &&
                    ((differentCurrencies && currencyIsoCodeFilter != null && !currencyIsoCodeFilter.equals(c.getCurrency())) ||
                     !differentCurrencies))
                    transferCurrencyList.add(currency);
            }
        }

        Collections.sort(transferCurrencyList, new CurrencyComparator());
        return transferCurrencyList;
    }

    private Currency findCurrencyByCode(String currencyCode) {
        return em.find(Currency.class, currencyCode);
    }

    public CalculateAmountCounterValueResponse getCalculateAmountCounterValue(BigDecimal amount, String currencySourceIsoCode,
                                                                              String currencyDestinationIsoCode, Date operationDate, BigDecimal rate,
                                                                              String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getCalculateAmountCounterValue(amount, currencySourceIsoCode, currencyDestinationIsoCode,
                                                                                         operationDate, rate, username, applic, language);
    }

    public CalculateAmountCounterValueResponse getCalculateAmountCounterValue(String sourceCurrencyIsoCode, BigDecimal amount,
                                                                              String destCurrencyIsoCode, String debitCurrencyIsoCode,
                                                                              BigDecimal rate, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getCalculateAmountCounterValue(sourceCurrencyIsoCode, amount, destCurrencyIsoCode,
                                                                                         debitCurrencyIsoCode, rate, username, applic, language);
    }


    public GetDefaultValueDatesResponse getCalculateDefautlValueDates(String directionCode, String applic, String currencyIsoCode,
                                                                      String accountNumber, String transferModeCode, String valueDateClass,
                                                                      Date operationDate, Date cutoffDate) {
        return getCalculateDefautlValueDates(directionCode, applic, currencyIsoCode, accountNumber, transferModeCode, valueDateClass, operationDate,
                                             cutoffDate, null, null, null);
    }

    // Overloaded , To delete the previous one later
    public GetDefaultValueDatesResponse getCalculateDefautlValueDates(String directionCode, String applic, String currencyIsoCode,
                                                                      String accountNumber, String transferModeCode, String valueDateClass,
                                                                      Date operationDate, Date cutoffDate, String username, String language,
                                                                      Date debitValueDate) {
        GetDefaultValueDatesResponse obj =
            paymentsTransferCommonModelSessionEJBLocal.getDefautlValueDates(directionCode, applic, currencyIsoCode, accountNumber, transferModeCode,
                                                                            valueDateClass, operationDate, cutoffDate, debitValueDate, username,
                                                                            language);
        return obj;
    }

    private CheckIs50fValidResponse checkIs50fValid(String orderingPCountryIsoCode, String orderingPAccount50k, String orderingPDebitAccountNumber,
                                                    String technicalFieldCode, String orderingPartyAddress1, String orderingPartyAddress2,
                                                    String orderingPartyName1, String orderingPartyName2, String orderingPartyPostalCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".valider_50f");

        call.addNamedArgument("p_i_isodo", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_cpt50k", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_compted", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_cur_item", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_adrdo1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_adrdo2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_dordred", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_dordred2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_codpostdo", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errcod", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_isodo");
        query.addArgument("p_i_cpt50k");
        query.addArgument("p_i_compted");
        query.addArgument("p_i_cur_item");
        query.addArgument("p_i_adrdo1");
        query.addArgument("p_i_adrdo2");
        query.addArgument("p_i_dordred");
        query.addArgument("p_i_dordred2");
        query.addArgument("p_i_codpostdo");

        List queryArgs = new ArrayList();
        queryArgs.add(orderingPCountryIsoCode);
        queryArgs.add(orderingPAccount50k);
        queryArgs.add(orderingPDebitAccountNumber);
        queryArgs.add(technicalFieldCode);
        queryArgs.add(orderingPartyAddress1);
        queryArgs.add(orderingPartyAddress2);
        queryArgs.add(orderingPartyName1);
        queryArgs.add(orderingPartyName2);
        queryArgs.add(orderingPartyPostalCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CheckIs50fValidResponse resp = new CheckIs50fValidResponse();
        resp.setFnReturn((String) record.get("p_o_errcod"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    private GetReportPaymentTypeResponse getReportPaymentType(String debitResidenceCode, String creditResidenceCode, BigDecimal transferAmount,
                                                              String debitAccountType, String creditAccountType, String debitEconomicAgent,
                                                              String creditEconomicAgent, String username, String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_GENERIC_PACKAGE_NAME + ".getReportPaymentType");

        call.addNamedArgument("p_i_residD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_residC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_amount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_accounttypD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_accounttypC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_economicAgentD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_economicAgentC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_CRPactive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_ReportPaymentType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_residD");
        query.addArgument("p_i_residC");
        query.addArgument("p_i_amount");
        query.addArgument("p_i_accounttypD");
        query.addArgument("p_i_accounttypC");
        query.addArgument("p_i_economicAgentD");
        query.addArgument("p_i_economicAgentC");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(debitResidenceCode);
        queryArgs.add(creditResidenceCode);
        queryArgs.add(transferAmount);
        queryArgs.add(debitAccountType);
        queryArgs.add(creditAccountType);
        queryArgs.add(debitEconomicAgent);
        queryArgs.add(creditEconomicAgent);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetReportPaymentTypeResponse resp = new GetReportPaymentTypeResponse();
        resp.setIsCRPActive((String) record.get("p_o_CRPactive"));
        resp.setReportPaymentType((String) record.get("p_o_ReportPaymentType"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setFnReturn((String) record.get("RESULT"));
        return resp;
        //        Query query = em.createNamedQuery("getReportPaymentType", GetReportPaymentTypeResponse.class);
        //        return (GetReportPaymentTypeResponse) query.setParameter("debitResidenceCode",
        //                                                                 debitResidenceCode).setParameter("creditResidenceCode",
        //                                                                                                  creditResidenceCode).setParameter("transferAmount",
        //                                                                                                                                                                                 transferAmount).setParameter("debitAccountType",
        //                                                                                                                                                                                                              debitAccountType).setParameter("creditAccountType",
        //                                                                                                                                                                                                                                             creditAccountType).setParameter("debitEconomicAgent",
        //                                                                                                                                                                                                                                                                             debitEconomicAgent).setParameter("creditEconomicAgent",
        //                                                                                                                                                                                                                                                                                                              creditEconomicAgent).setParameter("username",
        //                                                                                                                                                                                                                                                                                                                                                username).setParameter("applic",
        //                                                                                                                                                                                                                                                                                                                                                                       applic).setParameter("language",
        //                                                                                                                                                                                                                                                                                                                                                                                            language).getSingleResult();
    }

    private ControlChequeNumberResponse controlChequeNumber(String accountNumber, String chequeNumber, String applic) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".p_check_controls");

        call.addNamedArgument("p_i_account", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_reference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_account");
        query.addArgument("p_i_reference");
        query.addArgument("p_i_applic");

        List queryArgs = new ArrayList();
        queryArgs.add(accountNumber);
        queryArgs.add(chequeNumber);
        queryArgs.add(applic);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        ControlChequeNumberResponse resp = new ControlChequeNumberResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    public GetChequeNumAndRelativeRefAccessInfoResponse getChequeNumAndRelativeRefAccessInfo(String transferModeCode, String transferPeriodCode,
                                                                                             String debitLornosCode, String username, String applic,
                                                                                             String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_TRANSACTION_PACKAGE_NAME + ".getOrderingAccess");

        call.addNamedArgument("p_i_trsfMode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_periodicity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lornosd", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_User", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_ExistCHKNUM_XY", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_ChkRequired", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_ChkControls", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_chkMode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_accRelativeRef", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_accCheckNumber", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_trsfMode");
        query.addArgument("p_i_periodicity");
        query.addArgument("p_i_lornosd");
        query.addArgument("p_i_User");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_Lng");

        List queryArgs = new ArrayList();
        queryArgs.add(transferModeCode);
        queryArgs.add(transferPeriodCode);
        queryArgs.add(debitLornosCode);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetChequeNumAndRelativeRefAccessInfoResponse resp = new GetChequeNumAndRelativeRefAccessInfoResponse();
        resp.setIsChequeNumberEnabled((String) record.get("p_o_ExistCHKNUM_XY"));
        resp.setIsChequeNumberRequired((String) record.get("p_o_ChkRequired"));
        resp.setIsChequeNumberControlled((String) record.get("p_o_ChkControls"));
        resp.setChequeMode((String) record.get("p_o_chkMode"));
        resp.setAccRelativeRef((String) record.get("p_o_accRelativeRef"));
        resp.setAccChequeNumber((String) record.get("p_o_accCheckNumber"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setFnReturn((String) record.get("RESULT"));
        return resp;
    }

    public GetBdlZoneInformationResponse getBdlZoneInformation(String clientId, String transferModeCode) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_gClientdIc", JDBCTypes.VARCHAR_TYPE, clientId));
        params.add(new PlsqlInputParameter("p_i_modev", JDBCTypes.VARCHAR_TYPE, transferModeCode));
        params.add(new PlsqlOutputParameter("p_o_ZoneBdl", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_TriBdl", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_accZoneBdl", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLProcedure(PLSQL_UI_TRANSFER_PACKAGE_NAME, "virest_bdl_information", params, em);

        GetBdlZoneInformationResponse resp = new GetBdlZoneInformationResponse();
        String bdlZodeCode = (String) returnParams.get("p_o_ZoneBdl");
        if (bdlZodeCode != null)
            resp.setBdlZoneCode(findSwiftBDLZoneByCode(bdlZodeCode));
        resp.setBdlZodeSortCode((String) returnParams.get("p_o_TriBdl"));
        resp.setBdlZoneAccessCode((String) returnParams.get("p_o_accZoneBdl"));
        return resp;
    }

    public CheckIfDateIsOnWeekendResponse getCheckIfDateIsOnWeekend(Date dateToCheck, String countryIsoCode, String currencyIsoCode) {
        if (dateToCheck == null)
            return new CheckIfDateIsOnWeekendResponse(Boolean.valueOf(false));

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".week_end");

        call.addNamedArgument("p_i_date", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_iso", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_devise", JDBCTypes.VARCHAR_TYPE);
        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_date");
        query.addArgument("p_i_iso");
        query.addArgument("p_i_devise");

        List queryArgs = new ArrayList();
        queryArgs.add(dateToCheck);
        queryArgs.add(countryIsoCode);
        queryArgs.add(currencyIsoCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);
        Integer fnReturn = (Integer) record.get("RESULT");

        return new CheckIfDateIsOnWeekendResponse(fnReturn != null && fnReturn == 1 ? Boolean.valueOf(true) : Boolean.valueOf(false));
    }

    public CheckExchangeTicketInThresholdResponse getCheckExchangeTicketInThreshold(String strongCurrencyIsoCode, String weakCurrencyIsoCode,
                                                                                    String validityCode, String exchangeReference,
                                                                                    BigDecimal debitAmount, String debitCurrencyIsoCode,
                                                                                    String userAccessList, String referenceCurrencyIsoCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_CHAFIC_PACKAGE_NAME + ".seuil_authorise");

        call.addNamedArgument("p_a_dev1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_a_dev2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_a_valide", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_a_refchafic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_a_mntdevd", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_a_devised", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_wref_acces", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_global_devref", JDBCTypes.VARCHAR_TYPE);
        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_a_dev1");
        query.addArgument("p_a_dev2");
        query.addArgument("p_a_valide");
        query.addArgument("p_a_refchafic");
        query.addArgument("p_a_mntdevd");
        query.addArgument("p_a_devised");
        query.addArgument("p_wref_acces");
        query.addArgument("p_global_devref");

        List queryArgs = new ArrayList();
        queryArgs.add(strongCurrencyIsoCode);
        queryArgs.add(weakCurrencyIsoCode);
        queryArgs.add(validityCode);
        queryArgs.add(exchangeReference);
        queryArgs.add(debitAmount);
        queryArgs.add(debitCurrencyIsoCode);
        queryArgs.add(userAccessList);
        queryArgs.add(referenceCurrencyIsoCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);
        Integer fnReturn = (Integer) record.get("RESULT");

        return new CheckExchangeTicketInThresholdResponse(fnReturn != null && fnReturn == 1 ? Boolean.valueOf(true) : Boolean.valueOf(false));
    }

    public CheckExchangeAmountIsValidResponse getCheckExchangeAmountIsValid(String exchangeReference, BigDecimal exchangeAmount) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_CHAFIC_PACKAGE_NAME + ".TEST_MONTANT_CHAFIC");

        call.addNamedArgument("P_A_REFCHAFIC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_A_MNTDEVD", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);

        call.addNamedOutputArgument("P_ERRTXT", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("P_A_REFCHAFIC");
        query.addArgument("P_A_MNTDEVD");

        List queryArgs = new ArrayList();
        queryArgs.add(exchangeReference);
        queryArgs.add(exchangeAmount);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CheckExchangeAmountIsValidResponse resp = new CheckExchangeAmountIsValidResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("P_ERRTXT"));
        return resp;
    }

    //TODO delete me
    /*
    private RetrieveDefaultOrderingPartyAttributesResponse retrieveDefaultOrderinPartyAttributes(String clientId,
                                                                                                 String clientName,
                                                                                                 String clientIso,
                                                                                                 String username,
                                                                                                 String applic,
                                                                                                 String language) {

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName("pk_vir.Affecte_Dordred");

        call.addNamedArgument("p_i_client", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_nom", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_iso", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_dordred", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_dordred2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_adrdo1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_adrdo2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_iso", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_codpost", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_typecode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_message", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_client");
        query.addArgument("p_i_nom");
        query.addArgument("p_i_iso");

        List queryArgs = new ArrayList();
        queryArgs.add(clientId);
        queryArgs.add(clientName);
        queryArgs.add(clientIso);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        RetrieveDefaultOrderingPartyAttributesResponse resp = new RetrieveDefaultOrderingPartyAttributesResponse();

        resp.setOrderingPartyName1((String) record.get("p_o_dordred"));
        resp.setOrderingPartyName2((String) record.get("p_o_dordred2"));
        resp.setOrderingPartyAddress1((String) record.get("p_o_adrdo1"));
        resp.setOrderingPartyAddress2((String) record.get("p_o_adrdo2"));
        resp.setIso((String) record.get("p_o_iso"));
        resp.setPostalCode((String) record.get("p_o_codpost"));
        resp.setFnReturn((String) record.get("p_o_typecode"));
        resp.setErrorText((String) record.get("p_o_message"));

        return resp;


        //        Query query =
        //            em.createNamedQuery("retrieveDefaultOrderingPartyAttributes",
        //                                RetrieveDefaultOrderingPartyAttributesResponse.class);
        //        query.setParameter("clientId", clientId);
        //        query.setParameter("clientName", clientName);
        //        query.setParameter("iso", clientIso);
        //        return (RetrieveDefaultOrderingPartyAttributesResponse) query.getSingleResult();
    }
*/

    /**
     * Retireve default ordering party attributes for transfers
     * @param clientId clientId
     * @param applic applic
     * @param applic trimOrderingPartyAddressFlagtrimOrderingPartyAddressFlag
     * @return default ordering party attributes
     */
    public GetDefaultOrderingPartyAttributesResponse getDefaultOrderinPartyAttributes(String clientId, String applic,
                                                                                      String trimOrderingPartyAddressFlag) {
        return paymentsTransferCommonModelSessionEJBLocal.getDefaultOrderingPartyAttributes(clientId, null, trimOrderingPartyAddressFlag, applic);
    }

    /**
     * Retireve default ordering party attributes for transfers
     * @param clientId clientId
     * @param applic applic
     * @return default ordering party attributes
     */
    public GetDefaultOrderingPartyAttributesResponse getDefaultOrderinPartyAttributes(String clientId, String applic) {
        return paymentsTransferCommonModelSessionEJBLocal.getDefaultOrderingPartyAttributes(clientId, applic);
    }

    private CheckIsTillControlledForSwiftTransferModeResponse checkIsTillControlledForSwiftTransferMode(String transferModeCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".reponse_caisse");

        call.addNamedArgument("p_i_modev", JDBCTypes.VARCHAR_TYPE);
        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_modev");

        List queryArgs = new ArrayList();
        queryArgs.add(transferModeCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);
        Integer fnReturn = (Integer) record.get("RESULT");

        return new CheckIsTillControlledForSwiftTransferModeResponse(fnReturn != null && fnReturn == 1 ? Boolean.valueOf(true) :
                                                                     Boolean.valueOf(false));
    }

    private CheckIsTillModelExistentResponse checkIsTillModelExistent() {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".modele_caisse_existe");
        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);

        List results = (List) session.executeQuery(query, new ArrayList());
        DatabaseRecord record = (DatabaseRecord) results.get(0);
        Integer fnReturn = (Integer) record.get("RESULT");

        return new CheckIsTillModelExistentResponse(fnReturn == 1 ? Boolean.valueOf(true) : Boolean.valueOf(false));
    }


    private CheckIsAmountInLedgerLimitResponse checkIsAmountInLedgerLimit(BigDecimal amount, BigDecimal availableBalance, String currencyIsoCode,
                                                                          String ledgerCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".test_seuil_montant_rc");

        call.addNamedArgument("p_i_amount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_posdispd", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_devise", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Ncg", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_amount");
        query.addArgument("p_i_posdispd");
        query.addArgument("p_i_devise");
        query.addArgument("p_i_Ncg");

        List queryArgs = new ArrayList();
        queryArgs.add(amount);
        queryArgs.add(availableBalance);
        queryArgs.add(currencyIsoCode);
        queryArgs.add(ledgerCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CheckIsAmountInLedgerLimitResponse resp = new CheckIsAmountInLedgerLimitResponse();
        resp.setFnReturn((String) record.get("p_o_errcode"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    private CalculateRibKeyResponse calculateRibKey(String bankCode, String counterCode, String accountNumber) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName("a.clerib");

        call.addNamedArgument("pbanque", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("pguichet", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("pcompte", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("pcle", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("Perrtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("pbanque");
        query.addArgument("pguichet");
        query.addArgument("pcompte");

        List queryArgs = new ArrayList();
        queryArgs.add(bankCode);
        queryArgs.add(counterCode);
        queryArgs.add(accountNumber);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CalculateRibKeyResponse resp = new CalculateRibKeyResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("Perrtxt"));
        resp.setRibKey((String) record.get("pcle"));
        return resp;
        //        Query query = em.createNamedQuery("calculateRibKey", CalculateRibKeyResponse.class);
        //        query.setParameter("bankCode", bankCode);
        //        query.setParameter("counterCode", counterCode);
        //        query.setParameter("accountNumber", accountNumber);
        //        return (CalculateRibKeyResponse) query.getSingleResult();
    }

    private CalculateIbanFromRibKeyResponse calculateIbanFromRibKey(String bankCode, String counterCode, String accountNumber, String ribKey) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("Pcodbnq", JDBCTypes.VARCHAR_TYPE, bankCode));
        params.add(new PlsqlInputParameter("Pcodgch", JDBCTypes.VARCHAR_TYPE, counterCode));
        params.add(new PlsqlInputParameter("Pcompte", JDBCTypes.VARCHAR_TYPE, accountNumber));
        params.add(new PlsqlInputParameter("Pclerib", JDBCTypes.VARCHAR_TYPE, ribKey));
        params.add(new PlsqlOutputParameter("Perrtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript("pk_cpt", "Fiban_rib", params, em);

        CalculateIbanFromRibKeyResponse resp = new CalculateIbanFromRibKeyResponse();
        resp.setIban((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("Perrtxt"));
        resp.setFnReturn(returnParams.get("RESULT") == null ? "F" : "N");
        return resp;
    }

    private GetAccount50kFlagResponse getAccount50kFlag() {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".wref_fxycpt50k#val");

        call.addNamedOutputArgument("p_o_fxycpt50k", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errcod", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);

        List results = (List) session.executeQuery(query, new ArrayList());
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetAccount50kFlagResponse resp = new GetAccount50kFlagResponse();
        resp.setAccount50kFlag((String) record.get("p_o_fxycpt50k"));
        resp.setFnReturn((String) record.get("p_o_errcod"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    /*
     * LK 11.12.2014
     * Private util function to read y1 from fx5y8
     */
    private String getFxyValue(String tname, String model, String x1) {
        return getFxyValue(tname, model, x1, "Y1");
    }

    private String getFxyValue(String tname, String model, String x1, String returnCol) {
        returnCol = returnCol == null ? "Y1" : returnCol;

        List<String> allowedColList = new ArrayList<String>(Arrays.asList("Y1", "Y2", "Y3", "Y4", "Y5", "Y6"));
        if (!allowedColList.contains(returnCol.toUpperCase()))
            throw new IllegalArgumentException("Invalid FXY column");

        String sql = "SELECT " + returnCol + "   FROM fx5y8 WHERE tname= ?  AND model= ? AND x1 = ? ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, tname);
        query.setParameter(2, model);
        query.setParameter(3, x1);

        List<String> resultList = query.getResultList();
        return resultList != null && resultList.size() > 0 ? resultList.get(0) : null;
    }

    /*
     * LK 12.11.2014
     * Private function to fill the cheque number attributes from FXY
     */
    private ChequeAttributesBO getChequeAttributes(String applic) {
        String sql =
            "SELECT Y1, Y2, Y3, RTRIM(LTRIM(NVL(Y4,' '))) ||','||RTRIM(LTRIM(NVL(Y5,' ')))||','||RTRIM(LTRIM(NVL(Y6,' '))) " + "   FROM fx5y8 " +
            "   WHERE Tname = Decode(upper(?), 'VIRSEPA','VIRSEP', 'VIRCHAC','VIRCHA', upper(?)) " +
            "   AND Model = 'PARGEN' AND (substr(x1,1,1) != ' ' or x1 is null) AND X1 = 'NUMCHQ' ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, applic);
        query.setParameter(2, applic);

        List<Object[]> resultList = query.getResultList();
        String chequeNumbersActive = null;
        String chequeNumberRequired = null;
        String chequeNumberControlled = null;
        String chequeNumberTransferModes = null;
        if (resultList != null && resultList.size() > 0) {
            Object[] row = resultList.get(0);

            chequeNumbersActive = (String) row[0];
            chequeNumberRequired = (String) row[1];
            chequeNumberControlled = (String) row[2];
            chequeNumberTransferModes = (String) row[3];
        }

        ChequeAttributesBO checkAttributes = null;
        if ("O".equalsIgnoreCase(chequeNumbersActive)) {

            boolean isChequeNumberRequired = "O".equalsIgnoreCase(chequeNumberRequired);
            boolean isChequeNumberControlled = "O".equalsIgnoreCase(chequeNumberControlled);

            List<String> checkNumberTransferModeList = new ArrayList<String>(); //avoid null pointers
            if (chequeNumberTransferModes != null)
                checkNumberTransferModeList = Arrays.asList(chequeNumberTransferModes.split(","));

            checkAttributes = new ChequeAttributesBO();
            checkAttributes.setIsChequeNumberControlled(isChequeNumberControlled);
            checkAttributes.setIsChequeNumberRequired(isChequeNumberRequired);
            checkAttributes.setChequeTransferModeList(checkNumberTransferModeList);
        } else {
            checkAttributes = new ChequeAttributesBO();
            checkAttributes.setIsChequeNumberControlled(false);
            checkAttributes.setIsChequeNumberRequired(false);
            checkAttributes.setChequeTransferModeList(new ArrayList<String>());
        }
        return checkAttributes;
    }

    private boolean blockSwiftForCountryIso(String correspondentCountryIso, String beneficiaryCountryIso) {
        String sql =
            "SELECT 1 FROM fx5y8 WHERE tname='" + TransferTypeEnum.Swift.toString() + "' AND model='CTRLISO' " +
            "  AND UPPER(X1) = UPPER(?) AND UPPER(X2) = UPPER(?) AND NVL(UPPER(Y1), 'NON') = 'OUI' ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, correspondentCountryIso);
        query.setParameter(2, beneficiaryCountryIso);

        List<Object[]> resultList = query.getResultList();
        return resultList != null && resultList.size() > 0;
    }


    private SwiftAbbreviation findSwiftAbbreviationByFieldAndAbbr(String field, String abbreviation) {
        Query query = em.createNamedQuery("SwiftAbbreviation.findByFieldAndAbr", SwiftAbbreviation.class);
        query.setParameter("field", field).setParameter("abbreviation", abbreviation);

        List<SwiftAbbreviation> swiftAbbreviationList = query.getResultList();
        return swiftAbbreviationList != null && swiftAbbreviationList.size() >= 1 ? swiftAbbreviationList.get(0) : null;
    }

    public List<EnumLovItem> getYesNoList(String language, Boolean withEmptyValue) {
        return commonGenericCommonModelSessionEJBLocal.getYesNoList(language, withEmptyValue);
    }

    public List<EnumLovItem> getKeySwiftList(String language) {
        ResourceBundle bundle = ModelUtils.getBundle(PaymentsExternalTransferModelConstants.RESOURCE_BUNDLE_NAME, new Locale(language));
        return EnumUtils.enumValues(KeySwift.class, bundle);
    }

    //CRP
    public CalculateSirenCodeResponse getCalculateSirenCode(String paymentReportingCode, String debitResidenceCode, String creditResidenceCode,
                                                            String debitAccountNumber, String creditAccountNumber, String reportingPaymentTypeCode,
                                                            String username, String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_GENERIC_PACKAGE_NAME + ".getSirenCodeList");

        call.addNamedArgument("p_i_EcoCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ResidCodeD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ResidCodeC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_AccountD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_AccountC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ReportPaymentType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_SirenCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_EcoCode");
        query.addArgument("p_i_ResidCodeD");
        query.addArgument("p_i_ResidCodeC");
        query.addArgument("p_i_AccountD");
        query.addArgument("p_i_AccountC");
        query.addArgument("p_i_ReportPaymentType");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(paymentReportingCode);
        queryArgs.add(debitResidenceCode);
        queryArgs.add(creditResidenceCode);
        queryArgs.add(debitAccountNumber);
        queryArgs.add(creditAccountNumber);
        queryArgs.add(reportingPaymentTypeCode);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CalculateSirenCodeResponse resp = new CalculateSirenCodeResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setSirenCode((String) record.get("p_o_SirenCode"));
        return resp;
    }

    public List<PaymentReportCode> getPaymentReportCodeList() {
        return em.createNamedQuery("PaymentReportCode.findAll", PaymentReportCode.class).getResultList();
    }

    public List<ResidenceType> getResidenceTypeList(String beneficiaryBicCode, String beneficiaryCountryIsoCode, String beneficiaryBankBicCode,
                                                    String creditAccountNumber, String creditAccountTypeCode, String lornosc,
                                                    String correspondentCountryIsoCode, String creditClientAccount, String paymentReportingType,
                                                    String isLovFlag, String username, String applic, String lng) {
        //TODO: this function must be adapted to work on other transfer types later on.
        List<ResidenceType> residenceTypeList = null;
        ResidenceType residenceType = null;
        if (paymentReportingType != null && paymentReportingType.equalsIgnoreCase("RPC") && "O".equalsIgnoreCase(isLovFlag)) {

            residenceTypeList = em.createNamedQuery("ResidenceType.findAll", ResidenceType.class).getResultList();
            return residenceTypeList;
        } else {
            if (applic != null && applic.equalsIgnoreCase(TransferTypeEnum.Swift.toString())) {
                if (beneficiaryBicCode == null) {
                    if (beneficiaryCountryIsoCode == null) {
                        if (beneficiaryBankBicCode == null) {
                            Account correspondentAccount = findAccountByCode(beneficiaryCountryIsoCode);
                            if (correspondentAccount != null)
                                residenceType = correspondentAccount.getResidenceType();

                        } else {
                            BankBic beneficiaryBankBic = findBankBicByBic(beneficiaryBankBicCode);
                            if (beneficiaryBankBic != null)
                                residenceType = beneficiaryBankBic.getCountry().getResidenceType();

                        }
                    } else {
                        Country beneficiaryCountry = findCountryByCode(beneficiaryCountryIsoCode);
                        if (beneficiaryCountry != null)
                            residenceType = beneficiaryCountry.getResidenceType();
                    }
                } else {
                    BankBic beneficiaryBic = findBankBicByBic(beneficiaryBicCode);
                    if (beneficiaryBic != null)
                        residenceType = beneficiaryBic.getCountry().getResidenceType();
                }
            }
        }
        residenceTypeList = new ArrayList<ResidenceType>();
        if (residenceType != null)
            residenceTypeList.add(residenceType);

        return residenceTypeList;
    }

    //TODO might need to change the name: to be discussed with Cyril
    public List<PaymentReportCrpCode> getPaymentReportCrpCodeList() {
        return em.createNamedQuery("PaymentReportCrpCode.findAll", PaymentReportCrpCode.class).getResultList();
    }

    public List<BankCode> getBankCodeList(String genericFilter, String countryIsoCodeFilter, String applic) {
        Query query = em.createNamedQuery("BankCode.findBankCodeList", BankCode.class);
        return query.setParameter("genericFilter", genericFilter).setParameter("countryIsoCodeFilter", countryIsoCodeFilter).setParameter("applic",
                                                                                                                                          applic).getResultList();
    }

    public List<BankCounter> getBankCounterList(String bankCode, String genericFilter, String countryIsoCodeFilter) {
        Query query = em.createNamedQuery("BankCounter.findBankCounterList", BankCounter.class);
        return query.setParameter("genericFilter", genericFilter).setParameter("countryIsoCodeFilter", countryIsoCodeFilter).setParameter("bankCode",
                                                                                                                                          bankCode).getResultList();
    }

    public GetOrderingPartyDefaultAddressResponse getOrderingPartyDefaultAddress(String clientId, String economicAgentCode, String username,
                                                                                 String applic, String language) {
        //TODO this global functions calls a UI package, should review it later on
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_TRANSACTION_PACKAGE_NAME + ".getDefaultAddress");

        call.addNamedArgument("p_i_clientDId", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_EconomicAgent", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_OrderingAdress1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_OrderingAdress2", JDBCTypes.VARCHAR_TYPE);

        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_clientDId");
        query.addArgument("p_i_EconomicAgent");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(clientId);
        queryArgs.add(economicAgentCode);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetOrderingPartyDefaultAddressResponse resp = new GetOrderingPartyDefaultAddressResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setAddress1((String) record.get("p_o_OrderingAdress1"));
        resp.setAddress2((String) record.get("p_o_OrderingAdress2"));
        return resp;
    }


    private GetPaymentReportDefaultValuesResponse getPaymentReportDefaultValuesResponse(String transferModeCode, String crpActiveFlag,
                                                                                        String debitAccountNumber, String creditAccountNumber,
                                                                                        String creditCurrencyIsoCode, BigDecimal creditAmount,
                                                                                        String beneficiaryBicCode, String beneficiaryCityName,
                                                                                        String beneficiaryBankBicCode, String debitLornosCode,
                                                                                        String debitClientId, String creditLornosCode,
                                                                                        String creditClientId, String correspondentBicCode,
                                                                                        String correspondentCityName, String reportPaymentTypeCode,
                                                                                        String beneficiaryAccountNumber,
                                                                                        String beneficiaryBankAccountNumber, String beneficiaryName,
                                                                                        String orderingPartyBicCode, String orderingPartyCityName,
                                                                                        String infoToCorrespondentText, String debitAccountTypeCode,
                                                                                        String creditResidenceTypeCode, String debitResidenceTypeCode,
                                                                                        String creditAccountType, String sirenCode, String username,
                                                                                        String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_GENERIC_PACKAGE_NAME + ".setCRPValues");

        call.addNamedArgument("p_i_TransferMode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CRPactive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DebitAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CreditAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CreditCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CreditAmount", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_BeneficaryBIC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BeneficiaryCity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BeneficiaryBankBIC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DebitLornos", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DebitClientID", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CreditLornos", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CreditClientID", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CorrespondantBIC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CorrespondantCity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ReportPaymentType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BeneficiaryAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BeneficiaryBankAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BeneficiaryName", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DonorBIC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DonorCity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Mesncr", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_DebitorAccountType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedInOutputArgument("p_io_CreditorResidence", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("p_io_DebitorResidence", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("p_io_CreditorAccountType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("p_io_Siren", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_io_EcoCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_CRPCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_CountryCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_CRPBankCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_CRPBranchCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_TransferMode");
        query.addArgument("p_i_CRPactive");
        query.addArgument("p_i_DebitAccount");
        query.addArgument("p_i_CreditAccount");
        query.addArgument("p_i_CreditCurrency");
        query.addArgument("p_i_CreditAmount");
        query.addArgument("p_i_BeneficaryBIC");
        query.addArgument("p_i_BeneficiaryCity");
        query.addArgument("p_i_BeneficiaryBankBIC");
        query.addArgument("p_i_DebitLornos");
        query.addArgument("p_i_DebitClientID");
        query.addArgument("p_i_CreditLornos");
        query.addArgument("p_i_CreditClientID");
        query.addArgument("p_i_CorrespondantBIC");
        query.addArgument("p_i_CorrespondantCity");
        query.addArgument("p_i_ReportPaymentType");
        query.addArgument("p_i_BeneficiaryAccount");
        query.addArgument("p_i_BeneficiaryBankAccount");
        query.addArgument("p_i_BeneficiaryName");
        query.addArgument("p_i_DonorBIC");
        query.addArgument("p_i_DonorCity");
        query.addArgument("p_i_Mesncr");
        query.addArgument("p_i_DebitorAccountType");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");
        query.addArgument("p_io_CreditorResidence");
        query.addArgument("p_io_DebitorResidence");
        query.addArgument("p_io_CreditorAccountType");
        query.addArgument("p_io_Siren");

        List queryArgs = new ArrayList();
        queryArgs.add(transferModeCode);
        queryArgs.add(crpActiveFlag);
        queryArgs.add(debitAccountNumber);
        queryArgs.add(creditAccountNumber);
        queryArgs.add(creditCurrencyIsoCode);
        queryArgs.add(creditAmount);
        queryArgs.add(beneficiaryBicCode);
        queryArgs.add(beneficiaryCityName);
        queryArgs.add(beneficiaryBankBicCode);
        queryArgs.add(debitLornosCode);
        queryArgs.add(debitClientId);
        queryArgs.add(creditLornosCode);
        queryArgs.add(creditClientId);
        queryArgs.add(correspondentBicCode);
        queryArgs.add(correspondentCityName);
        queryArgs.add(reportPaymentTypeCode);
        queryArgs.add(beneficiaryAccountNumber);
        queryArgs.add(beneficiaryBankAccountNumber);
        queryArgs.add(beneficiaryName);
        queryArgs.add(orderingPartyBicCode);
        queryArgs.add(orderingPartyCityName);
        queryArgs.add(infoToCorrespondentText);
        queryArgs.add(debitAccountTypeCode);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);
        queryArgs.add(creditResidenceTypeCode);
        queryArgs.add(debitResidenceTypeCode);
        queryArgs.add(creditAccountType);
        queryArgs.add(sirenCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        String _bankCode = (String) record.get("p_o_CRPBankCode");
        String _counterCode = (String) record.get("p_o_CRPBranchCode");
        String _countryCode = (String) record.get("p_o_CountryCode");
        String _creditResidenceCode = (String) record.get("p_io_CreditorResidence");
        String _debitResidenceCode = (String) record.get("p_io_DebitorResidence");
        String _paymentReportCode = (String) record.get("p_io_EcoCode");
        String _paymentReportCrpCode = (String) record.get("p_o_CRPCode");

        GetPaymentReportDefaultValuesResponse resp = new GetPaymentReportDefaultValuesResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));

        if (!resp.hasError()) {
            if (!StringUtils.isEmpty(_paymentReportCode)) {
                PaymentReportCode paymentReportCode = em.find(PaymentReportCode.class, _paymentReportCode);
                resp.setPaymentReportCode(paymentReportCode);
            }

            if (!StringUtils.isEmpty(_paymentReportCrpCode)) {
                PaymentReportCrpCode paymentReportCrpCode = em.find(PaymentReportCrpCode.class, _paymentReportCrpCode);
                resp.setPaymentReportCrpCode(paymentReportCrpCode);
            }

            if (!StringUtils.isEmpty(_bankCode)) {
                BankCode bankCode = em.find(BankCode.class, _bankCode);
                resp.setBankCode(bankCode);
            }

            if (!StringUtils.isEmpty(_counterCode)) {
                BankCounterPK bankCounterPK = new BankCounterPK();
                bankCounterPK.setBankCode(_bankCode);
                bankCounterPK.setCode(_counterCode);
                BankCounter bankCounter = em.find(BankCounter.class, bankCounterPK);
                resp.setBankCounter(bankCounter);
            }

            if (!StringUtils.isEmpty(_countryCode)) {
                Country country = findCountryByCode(_countryCode);
                resp.setCountry(country);
            }

            resp.setSirenCode((String) record.get("p_io_Siren"));
            resp.setCreditAccountTypeCode((String) record.get("p_io_CreditorAccountType"));

            if (!StringUtils.isEmpty(_creditResidenceCode))
                resp.setCreditResidenceCode(findResidenceTypeByCode(_creditResidenceCode));

            if (!StringUtils.isEmpty(_debitResidenceCode))
                resp.setDebitResidenceCode(findResidenceTypeByCode(_debitResidenceCode));
        }
        return resp;
    }

    //TODO chaange my name
    private GetOrderingPartyNameAndCityResponse getOrderingPartyNameAndCity(String clientId, String clientName, String cityName) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".find_adr");

        call.addNamedArgument("pclient", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("pnom", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("pville", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errcod", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("pclient");
        query.addArgument("pnom");
        query.addArgument("pville");

        List queryArgs = new ArrayList();
        queryArgs.add(clientId);
        queryArgs.add(clientName);
        queryArgs.add(cityName);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetOrderingPartyNameAndCityResponse resp = new GetOrderingPartyNameAndCityResponse();
        resp.setFnReturn((String) record.get("p_o_errcod"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setOrderingPartyName((String) record.get("pnom"));
        resp.setCityName((String) record.get("pville"));
        return resp;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private UserAuthorizedApplic getUserAuthorizedApplicForUserAndApplic(User user, String applic) {
        TypedQuery<UserAuthorizedApplic> query =
            em.createNamedQuery("UserAuthorizedApplic.findUserAuthorizedApplicForUserAndApplic", UserAuthorizedApplic.class);
        query.setParameter("usercode", user.getUsercode());
        query.setParameter("applic", applic);
        List<UserAuthorizedApplic> userAuthorizedApplicList = query.getResultList();

        return userAuthorizedApplicList != null && userAuthorizedApplicList.size() > 0 ? userAuthorizedApplicList.get(0) : null;
    }

    public GenerateOperationReferenceResponse getGenerateOperationReference() {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName("a.PNOOPER_AUTONOME");

        call.addNamedInOutputArgument("p_nooper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_texterr", JDBCTypes.VARCHAR_TYPE);
        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_nooper");

        List queryArgs = new ArrayList();
        queryArgs.add(null);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        Integer fnReturn = (Integer) record.get("RESULT");

        GenerateOperationReferenceResponse resp = new GenerateOperationReferenceResponse();
        // System.out.println("NOOER=" + operationReference);
        resp.setFnReturn(fnReturn != null && fnReturn.intValue() == 1 ? "N" : "F");
        resp.setErrorText((String) record.get("p_texterr"));
        resp.setOperationReference((String) record.get("p_nooper"));
        return resp;
    }

    private SetOperationWeightResponse setOperationWeight(String operationReference, String applic) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("P_I_Applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("P_I_Refer", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlOutputParameter("P_O_Errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "SetOperationWeight", params, em);

        SetOperationWeightResponse resp = new SetOperationWeightResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("P_O_Errtxt"));
        return resp;
    }

    private ValidateWeightSignatureResponse validateWeightSignature(String operationReference, Integer validationNumber,
                                                                    String validateMonitoringFlag, String coreShellProcessFlag, String printerCode,
                                                                    String username, String applic, String language) {
        return validateWeightSignature(operationReference, null, validationNumber, validateMonitoringFlag, coreShellProcessFlag, printerCode,
                                       "dobatch", null, username, applic, language);
    }

    private ValidateWeightSignatureResponse validateWeightSignature(String operationReference, BigDecimal orderNumber, Integer validationNumber,
                                                                    String validateMonitoringFlag, String coreShellProcessFlag, String printerCode,
                                                                    String shellCommand, String initialReference, String username, String applic,
                                                                    String language) {
        return validateWeightSignature(operationReference, orderNumber, validationNumber, validateMonitoringFlag, coreShellProcessFlag, printerCode,
                                       shellCommand, initialReference, null, username, applic, language);
    }

    private ValidateWeightSignatureResponse validateWeightSignature(String operationReference, BigDecimal orderNumber, Integer validationNumber,
                                                                    String validateMonitoringFlag, String coreShellProcessFlag, String printerCode,
                                                                    String shellCommand, String initialReference, BigDecimal swiftVersionNumber,
                                                                    String username, String applic, String language) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("P_I_Refer", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlInputParameter("p_i_numOrdre", JDBCTypes.NUMERIC_TYPE, orderNumber));
        params.add(new PlsqlInputParameter("P_I_ValidationNum", JDBCTypes.NUMERIC_TYPE, validationNumber));
        params.add(new PlsqlInputParameter("P_I_Valid_DV_DS", JDBCTypes.VARCHAR_TYPE, validateMonitoringFlag));
        params.add(new PlsqlInputParameter("p_i_CoreShellProcess", JDBCTypes.VARCHAR_TYPE, coreShellProcessFlag));
        params.add(new PlsqlInputParameter("p_i_printerCode", JDBCTypes.VARCHAR_TYPE, printerCode));
        params.add(new PlsqlInputParameter("p_i_shellCommand", JDBCTypes.VARCHAR_TYPE, shellCommand));
        params.add(new PlsqlInputParameter("p_i_initReference", JDBCTypes.VARCHAR_TYPE, initialReference));
        params.add(new PlsqlInputParameter("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE, swiftVersionNumber));
        params.add(new PlsqlInputParameter("P_I_User", JDBCTypes.VARCHAR_TYPE, username));
        params.add(new PlsqlInputParameter("P_I_Applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("P_I_Lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "validateWeightSignature", params, em);

        ValidateWeightSignatureResponse resp = new ValidateWeightSignatureResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        return resp;
    }

    public CalculateBankAccountIbanResponse calculateBankAccountIban(String bankAccountNumber) {
        return paymentsTransferCommonModelSessionEJBLocal.calculateBankAccountIban(bankAccountNumber);
    }

    private GetOperationAmountsResponse getOperationAmounts(String validityCode, String operationMode, BigDecimal exchangeRate,
                                                            String strongCurrencyIsoCode, String weakCurrencyIsoCode, String operationReference,
                                                            String debitAccountNumber, String debitCurrencyIsoCode, BigDecimal debitAmount,
                                                            String creditAccountNumber, String creditCurrencyIsoCode, BigDecimal creditAmount, String exchangeRateFlag //TODO exchange rate flag
                                                            ) {
        return getOperationAmounts(validityCode, operationMode, exchangeRate, strongCurrencyIsoCode, weakCurrencyIsoCode, operationReference,
                                   debitAccountNumber, debitCurrencyIsoCode, debitAmount, creditAccountNumber, creditCurrencyIsoCode, creditAmount, exchangeRateFlag //TODO exchange rate flag
                                   , null, null, null, null, null, null);
    }

    private GetOperationAmountsResponse getOperationAmounts(String validityCode, String operationMode, BigDecimal exchangeRate,
                                                            String strongCurrencyIsoCode, String weakCurrencyIsoCode, String operationReference,
                                                            String debitAccountNumber, String debitCurrencyIsoCode, BigDecimal debitAmount,
                                                            String creditAccountNumber, String creditCurrencyIsoCode, BigDecimal creditAmount, String exchangeRateFlag //TODO exchange rate flag
                                                            , BigDecimal debitNetAmount, BigDecimal creditNetAmount, String operationCode,
                                                            String operationPositionCode, BigDecimal overdraftAmount,
                                                            BigDecimal overdraftCounterValueAmount) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName("pk_vir.troper");

        call.addNamedArgument("P_i_valide", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_modev", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_cours12", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("P_i_dev1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_dev2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_nooper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_compted", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_devised", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_mntdevd", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("P_i_comptec", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_devisec", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("P_i_mntdevc", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_coursok", JDBCTypes.VARCHAR_TYPE);

        call.addNamedInOutputArgument("P_io_mnttotdo", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedInOutputArgument("P_io_mnttotbe", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedInOutputArgument("P_io_oper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("P_io_xposoper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("P_io_depas", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedInOutputArgument("P_io_cvmntdep", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedInOutputArgument("p_io_typecode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedInOutputArgument("p_io_message", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("P_i_valide");
        query.addArgument("P_i_modev");
        query.addArgument("P_i_cours12");
        query.addArgument("P_i_dev1");
        query.addArgument("P_i_dev2");
        query.addArgument("P_i_nooper");
        query.addArgument("P_i_compted");
        query.addArgument("P_i_devised");
        query.addArgument("P_i_mntdevd");
        query.addArgument("P_i_comptec");
        query.addArgument("P_i_devisec");
        query.addArgument("P_i_mntdevc");
        query.addArgument("P_io_mnttotdo");
        query.addArgument("P_io_mnttotbe");
        query.addArgument("P_io_oper");
        query.addArgument("P_io_xposoper");
        query.addArgument("P_io_depas");
        query.addArgument("P_io_cvmntdep");
        query.addArgument("p_io_typecode");
        query.addArgument("p_io_message");
        query.addArgument("p_i_coursok");

        List queryArgs = new ArrayList();
        queryArgs.add(validityCode);
        queryArgs.add(operationMode);
        queryArgs.add(exchangeRate);
        queryArgs.add(strongCurrencyIsoCode);
        queryArgs.add(weakCurrencyIsoCode);
        queryArgs.add(operationReference);
        queryArgs.add(debitAccountNumber);
        queryArgs.add(debitCurrencyIsoCode);
        queryArgs.add(debitAmount);
        queryArgs.add(creditAccountNumber);
        queryArgs.add(creditCurrencyIsoCode);
        queryArgs.add(creditAmount);
        queryArgs.add(debitNetAmount);
        queryArgs.add(creditNetAmount);
        queryArgs.add(operationCode);
        queryArgs.add(operationPositionCode);
        queryArgs.add(overdraftAmount);
        queryArgs.add(overdraftCounterValueAmount);
        queryArgs.add(null); //fnReturn
        queryArgs.add(null); //ErrorText
        queryArgs.add(exchangeRateFlag);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetOperationAmountsResponse resp = new GetOperationAmountsResponse();
        resp.setFnReturn((String) record.get("p_io_typecode"));
        resp.setErrorText((String) record.get("p_io_message"));
        resp.setDebitNetAmount((BigDecimal) record.get("P_io_mnttotdo"));
        resp.setCreditNetAmount((BigDecimal) record.get("P_io_mnttotbe"));
        resp.setOperationCode((String) record.get("P_io_oper"));
        resp.setOperationPositionCode((String) record.get("P_io_xposoper"));
        resp.setOverdraftAmount((BigDecimal) record.get("P_io_depas"));
        resp.setOverdraftCounterValueAmount((BigDecimal) record.get("P_io_cvmntdep"));
        return resp;
    }

    private GetDefaultAdviceGenerationTypeResponse getDefaultAdviceGenerationType(String applic, String userServiceCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".rch_fxy_tgavi");

        call.addNamedArgument("Papplic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("Pcserv", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("Ptypgen", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errcod", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("Papplic");
        query.addArgument("Pcserv");

        List queryArgs = new ArrayList();
        queryArgs.add(applic);
        queryArgs.add(userServiceCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetDefaultAdviceGenerationTypeResponse resp = new GetDefaultAdviceGenerationTypeResponse();
        resp.setFnReturn((String) record.get("p_o_errcod"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setAdviceGenerationType((String) record.get("Ptypgen"));
        return resp;
    }

    private GetCommissionDetailsResponse getCommissionDetails(String operationRefernece, String debitCurrencyIsoCode, String creditCurrencyIsoCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName("pk_globalws_commission.Commissions_Details");
        call.addNamedArgument("p_i_Nooper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Devised", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Devisec", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_Mntcion1", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedOutputArgument("p_o_Mntcion2", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedOutputArgument("p_o_Percep", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_Xcomobl", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errcod", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_Nooper");
        query.addArgument("p_i_Devised");
        query.addArgument("p_i_Devisec");

        List queryArgs = new ArrayList();
        queryArgs.add(operationRefernece);
        queryArgs.add(debitCurrencyIsoCode);
        queryArgs.add(creditCurrencyIsoCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetCommissionDetailsResponse resp = new GetCommissionDetailsResponse();
        resp.setCommissionAmount1((BigDecimal) record.get("p_o_Mntcion1"));
        resp.setCommissionAmount2((BigDecimal) record.get("p_o_Mntcion2"));
        resp.setPerception((String) record.get("p_o_Percep"));
        resp.setMandatoryCommissionFlag((String) record.get("p_o_Xcomobl"));
        resp.setFnReturn((String) record.get("p_o_errcod"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    private VerifyPaymentReportingValuesResponse verifyPaymentReportingValues(String paymentReportCode, String paymentReportBankCode,
                                                                              String sirenCode, String isPaymentReportActive,
                                                                              String paymentReportType, String paymentReportCrpCode, String username,
                                                                              String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_GENERIC_PACKAGE_NAME + ".verifyCRPCode");

        call.addNamedArgument("p_i_EcoCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BankCRP", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_SIREN", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CRPactive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ReportPaymentType", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_CRPCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);
        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_EcoCode");
        query.addArgument("p_i_BankCRP");
        query.addArgument("p_i_SIREN");
        query.addArgument("p_i_CRPactive");
        query.addArgument("p_i_ReportPaymentType");
        query.addArgument("p_i_CRPCode");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(paymentReportCode);
        queryArgs.add(paymentReportBankCode);
        queryArgs.add(sirenCode);
        queryArgs.add(isPaymentReportActive);
        queryArgs.add(paymentReportType);
        queryArgs.add(paymentReportCrpCode);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        VerifyPaymentReportingValuesResponse resp = new VerifyPaymentReportingValuesResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    public List<Applic> testElioJPQL() {
        String jpql = "Select a from Applic a LEFT JOIN a.applicAllowedActionList aaal where a.applicCode = 'CPTVUE' ";
        Query query = em.createQuery(jpql, Applic.class);

        Session session = em.unwrap(JpaEntityManager.class).getActiveSession();
        DatabaseQuery databaseQuery = ((EJBQueryImpl) query).getDatabaseQuery();
        databaseQuery.prepareCall(session, new DatabaseRecord());
        String sqlString = databaseQuery.getSQLString();
        System.out.println("THE SQL STRING IS ");
        System.out.println(sqlString);
        return query.getResultList();
    }

    private ProcessPaidChequeResponse processPaidCheque(String accountNumber, String chequeNumber, Date operationDate) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName("pk_ctrlchq.Pr_tt_chqpay");
        call.addNamedArgument("Pcompte", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("Pchqref", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("Pdatoper", JDBCTypes.DATE_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("Pcompte");
        query.addArgument("Pchqref");
        query.addArgument("Pdatoper");

        List queryArgs = new ArrayList();
        queryArgs.add(accountNumber);
        queryArgs.add(chequeNumber);
        queryArgs.add(operationDate);

        List results = (List) session.executeQuery(query, queryArgs);
        //DatabaseRecord record = (DatabaseRecord) results.get(0);

        ProcessPaidChequeResponse resp = new ProcessPaidChequeResponse();
        resp.setFnReturn("N"); //TODO: check comments on  ProcessPaidChequeResponse
        return resp;
    }

    private ControlAMLResponse controlAML(String applic, String operationReference, String calculationMode, String moneyLaunderingFlag) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".ControlBlanchiment");

        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_nooper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_mode", JDBCTypes.VARCHAR_TYPE);

        call.addNamedInOutputArgument("p_io_blanchiment", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_applic");
        query.addArgument("p_i_nooper");
        query.addArgument("p_i_mode");
        query.addArgument("p_io_blanchiment");

        List queryArgs = new ArrayList();
        queryArgs.add(applic);
        queryArgs.add(operationReference);
        queryArgs.add(calculationMode);
        queryArgs.add(moneyLaunderingFlag);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        ControlAMLResponse resp = new ControlAMLResponse();

        String _moneyLaunderingFlag = ((String) record.get("p_io_blanchiment"));
        resp.setMoneyLaunderingFlag(_moneyLaunderingFlag);
        resp.setErrorText((String) record.get("p_o_errtxt"));

        String fnReturn = _moneyLaunderingFlag;
        if ("A".equalsIgnoreCase(_moneyLaunderingFlag) || "O".equalsIgnoreCase(_moneyLaunderingFlag))
            fnReturn = "N".equalsIgnoreCase(calculationMode) ? "N" /*Informative eror*/ : "F" /*Blocking error*/;
        resp.setFnReturn(fnReturn);
        return resp;
    }

    private GetAmountDescriptionResponse getAmountDescription(String currencyIsoCode, String languageCode) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".GetMntletr");
        call.addNamedArgument("P_Curreny", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_language", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_answerAck", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_answerTxt", JDBCTypes.VARCHAR_TYPE);
        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("P_Curreny");
        query.addArgument("p_language");

        List queryArgs = new ArrayList();
        queryArgs.add(currencyIsoCode);
        queryArgs.add(languageCode);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        GetAmountDescriptionResponse resp = new GetAmountDescriptionResponse();
        resp.setFnReturn((String) record.get("p_o_answerAck"));
        resp.setErrorText((String) record.get("p_o_answerTxt"));
        resp.setDescription((String) record.get("RESULT"));
        return resp;
    }

    private CheckIsBDLAmountInLimitResponse checkIsBDLAmountInLimit(String clientId, String currency, BigDecimal netAmount, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_TRANSFER_PACKAGE_NAME + ".TEST_SEUIL_MONTANT_BDL");

        call.addNamedArgument("p_i_clientc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_devisec", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_mnttotbe", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);

        call.setResult(OraclePLSQLTypes.PLSQLBoolean);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_clientc");
        query.addArgument("p_i_devisec");
        query.addArgument("p_i_mnttotbe");

        List queryArgs = new ArrayList();
        queryArgs.add(clientId);
        queryArgs.add(currency);
        queryArgs.add(netAmount);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        Integer fnReturn = (Integer) record.get("RESULT");

        CheckIsBDLAmountInLimitResponse resp = new CheckIsBDLAmountInLimitResponse();
        if (fnReturn != null && fnReturn.intValue() == 0)
            resp.setFnReturn("OK");
        else {
            resp.setFnReturn("KO");
            resp.setErrorText(translate("MSG_INVALID_BDL_AMOUNT", language));
        }
        return resp;
    }

    public void getCurrencyRate(String currencyIso) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName("TST_ORACLE.GETCURRENCYRATE");
        call.addNamedArgument("P_I_CCY2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("P_O_RATE", JDBCTypes.NUMERIC_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("P_I_CCY2");

        List queryArgs = new ArrayList();
        queryArgs.add(currencyIso);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);
    }


    public GetBankUserActivityListResponse getUserInfo(String username) {
        GetBankUserActivityListResponse bankUserActivityListResponse = new GetBankUserActivityListResponse();
        bankUserActivityListResponse.setFnReturn("N");

        LegacyUser legacyUser = em.createNamedQuery("LegacyUser.findByUsername", LegacyUser.class).setParameter("uname", username).getSingleResult();
        if (legacyUser != null && legacyUser.getExpl() != null)
            bankUserActivityListResponse = commonGenericCommonModelSessionEJBLocal.getBankUserActivityList(legacyUser.getExpl());

        return bankUserActivityListResponse;
    }

    public List<TransferBO> getExternalTransferBOList(String clientId, String referenceFilter, Date fromDateFilter, Date toDateFilter,
                                                      String benefNameFilter, String benefBankCounterFilter, String benefBankBicFilter,
                                                      String periodicityFilter, String statusFilter, String userActivityCode,
                                                      String beneficiaryCountry, String transferCurrencyFilter, String username, String applic,
                                                      String language, String transferTypeCode, String isTreasuryTransferHP,
                                                      String modeTransferFilter, String serviceFilter, String profileFlag, String teamFlag) {
        String sql =
            "SELECT TypeCode, TypeDesc, Reference operationReference, BenefName benefName, BenefBank benefBankName, " +
            "benefBankCountry benefBankCountryName, Account debitAccountNumber, Currency debitCurrencyIso, " +
            "Amount debitAmount, OperationDate operationDate, accountingdate accountingdate, " +
            "PeriodCode periodicityCode, PeriodDesc periodicityDesc, Status statusCode, StatusDesc statusDesc, " +
            "isCancelAllowed cancelAllowedFlag, isValidateAllowed validateAllowedFlag,isViewSwiftAllowed viewSwiftAllowedFlag, " +
            "isModifyAllowed modifyAllowedFlag, isDuplicateAllowed duplicateAllowedFlag, isRecallSepaAllowed recallAllowedFlag," +
            "modifyCancelSepaFlag modifyCancelSepaAllowedFlag, cancelValidRecallFlag cancelValidRecallAllowed, validateRecallSepaFlag validateRecallSepaAllowed, " +
            "nooperMod modReference, cancelSepaTrnsfExists, initiator,firstValidator,secondValidator,thirdValidator, version, depass, position, interdiction, dordred orderingPartyName1, dordred2 orderingPartyName2 FROM TABLE(" +
            PLSQL_GLOBAL_PACKAGE_NAME + ".getExternalPaymentList(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?))";

        if (clientId != null && clientId.isEmpty())
            clientId = null;

        List<Object> params = new ArrayList<Object>();
        params.add(clientId);
        params.add(referenceFilter);
        params.add(fromDateFilter);
        params.add(toDateFilter);
        params.add(benefNameFilter);
        params.add(benefBankCounterFilter);
        params.add(benefBankBicFilter);
        params.add(transferTypeCode);
        params.add(periodicityFilter);
        params.add(statusFilter);
        params.add(userActivityCode);
        params.add(beneficiaryCountry);
        params.add(transferCurrencyFilter);
        params.add(isTreasuryTransferHP);
        params.add(modeTransferFilter);
        params.add(serviceFilter);
        params.add(profileFlag);
        params.add(teamFlag);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<TransferBO> transferBOList = QueryUtils.executeNativeQuery(sql, TransferBO.class, params, em);
        return transferBOList;
    }

    public GetValidateTransferResponse getValidateTransfer(String applic, String user, String refer, Integer validationNo, String valid,
                                                           String coreShellProcess, String language, String printerCode) {
        PLSQLGlobalFunctionResponse preValidResponse = null;
        if (TransferTypeEnum.Swift.toString().equals(applic))
            preValidResponse = swiftTransferPreValidate(refer, user, applic, language);
        else if (TransferTypeEnum.Sepa.toString().equals(applic))
            preValidResponse = sepaTransferPreValidate(refer, user, applic, language);
        else if (TransferTypeEnum.Clearing.toString().equals(applic))
            preValidResponse = clearingTransferPreValidate(refer, user, applic, language);

        if (preValidResponse != null && preValidResponse.hasError()) {
            GetValidateTransferResponse finalResp = new GetValidateTransferResponse();
            finalResp.setFnReturn(preValidResponse.getFnReturn());
            finalResp.setErrorText(preValidResponse.getErrorText());
            return finalResp;
        }
        return paymentsTransferCommonModelSessionEJBLocal.getValidateTransfer(applic, user, refer, validationNo, valid, coreShellProcess, language,
                                                                              printerCode);
    }

    public GetTransferStatusResponse getTranferStatus(String reference, String user, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getTranferStatus(reference, user, applic, language);
    }

    public GetCancelTransferResponse getCancelTransfer(String operationReferece, String user, String applic, String language, String printerCode) {
        return paymentsTransferCommonModelSessionEJBLocal.getCancelTransfer(operationReferece, user, applic, language, printerCode);
    }

    /**
     * Get the user default till if any
     * @param transferModeCode
     * @param username
     * @param applic
     * @return Standard UI PLSQL fucntion response containing the user default till
     */
    public GetUserDefaultTillResponse getUserDefaultTill(String transferModeCode, String username, String applic) {
        return paymentsTransferCommonModelSessionEJBLocal.getUserDefaultTill(transferModeCode, username, applic);
    }

    /**
     * Returns the list transfer Modes
     * @param userUnauthorizedOperList List of unauthorized operations for the user (coma separated)
     * @param userAuthorizedOperList List of authorized operations for the user (coma separated)
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of transfer statuses
     */
    public List<TransferModeBO> getTransferModeBOList(String userUnauthorizedOperList, String userAuthorizedOperList, String callingTf,
                                                      String username, String applic, String language) {
        List<TransferModeBO> transferModeBOList =
            paymentsTransferCommonModelSessionEJBLocal.getTransferModeBOList(userUnauthorizedOperList, userAuthorizedOperList, callingTf, username,
                                                                             applic, language);
        return transferModeBOList;
    }

    /**
     * Returns the list transfer Periodicities
     * @param permanentTransferFlag flag to include if the required list is for permanent transfers
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of transfer periodicities
     */
    public List<TransferPeriodicityBO> getTransferPeriodicityBOList(String permanentTransferFlag, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getTransferPeriodicityBOList(permanentTransferFlag, username, applic, language);
    }

    public List<TransferAccountBO> getDebitTransferAccountBOList(String clientId, String ledgerGroupCodeFilter, String activityFilter,
                                                                 String currencyIsoCodeFilter, String genericFilter, String bacsUserActiveFlag,
                                                                 String username, String applic, String language) {
        List<TransferAccountBO> transferAccountBOList =
            getTransferAccountBOList(clientId, ledgerGroupCodeFilter, "D", activityFilter, currencyIsoCodeFilter, genericFilter, bacsUserActiveFlag,
                                     username, applic, language);
        return transferAccountBOList;
    }

    /**
     * Returns the list accounts for transfers
     * @param clientId Client Id
     * @param ledgerGroupCodeFilter Account ledger group
     * @param directionCode D: Debit, C:Credit
     * @param activityFilter activity Filter
     * @param currencyIsoCodeFilter optional filter on currency
     * @param genericFilter optional generic filter (account number or account name)
     * @param bacsUserActiveFlag flag if bacsUser is active
     * @param username logged in username
     * @param applic applic
     * @param language language
     * @return list of transfer accounts
     */
    public List<TransferAccountBO> getTransferAccountBOList(String clientId, String ledgerGroupCodeFilter, String directionCode,
                                                            String activityFilter, String currencyIsoCodeFilter, String genericFilter,
                                                            String bacsUserActiveFlag, String username, String applic, String language) {
        List<TransferAccountBO> transferAccountBOList =
            paymentsTransferCommonModelSessionEJBLocal.getTransferAccountBOList(clientId, ledgerGroupCodeFilter, directionCode, activityFilter,
                                                                                currencyIsoCodeFilter, genericFilter, bacsUserActiveFlag, username,
                                                                                applic, language);
        return transferAccountBOList;
    }

    @Override
    public SaveCommissionListResponse saveCommissionList(List<GlobalCommissionBO> commissionList, String debitAccount, String creditAccount,
                                                         String debitCurrencyCode, String creditCurrencyCode, String userCode, String userService,
                                                         String operationNumber, Date operationDate, String username, String applic, String language,
                                                         String wizardMode) {
        SaveCommissionListResponse resp =
            commissionSessionEJB.saveCommissionList(commissionList, debitAccount, creditAccount, debitCurrencyCode, creditCurrencyCode, userCode,
                                                    userService, operationNumber, operationDate, username, applic, language);
        return resp;
    }

    @Override
    public void deleteOperationCommissionList(String applic, String operationNumber) {
        commissionSessionEJB.deleteOperationCommissionList(applic, operationNumber);
    }

    public SepaTransfeSummaryContextBO getSepaTransferSummaryContextBO(String operationReference, BigDecimal debitAmount, BigDecimal creditAmount,
                                                                       String debitAccount, String creditAccount, String username, String applic,
                                                                       String language) {
        SepaTransfeSummaryContextBO sepaTrsfSummaryctx = new SepaTransfeSummaryContextBO();

        List<GlobalCommissionBO> globalcommissionBoList = new ArrayList<GlobalCommissionBO>();
        SepaTransferBO sepaTransferSummaryBO = getSepaTransferBO(operationReference, username, applic, language);
        if (sepaTransferSummaryBO != null) {
            globalcommissionBoList =
                getOperationCommissionList(operationReference, sepaTransferSummaryBO.getDebitAmount(), sepaTransferSummaryBO.getCreditAmount(),
                                           sepaTransferSummaryBO.getDebitAccount(), sepaTransferSummaryBO.getCreditAccount(), username, applic,
                                           language);
        }

        sepaTrsfSummaryctx.setGlobalCommissionList(globalcommissionBoList);
        sepaTrsfSummaryctx.setSepaTransferBO(sepaTransferSummaryBO);
        return sepaTrsfSummaryctx;
    }

    public ClearingTransferSummaryContextBO getClearingTransferSummaryContextBO(String operationReference, BigDecimal debitAmount,
                                                                                BigDecimal creditAmount, String debitAccount, String creditAccount,
                                                                                String username, String applic, String language) {
        ClearingTransferSummaryContextBO clearingTrsfSummaryctx = new ClearingTransferSummaryContextBO();
        List<GlobalCommissionBO> globalcommissionBoList = new ArrayList<GlobalCommissionBO>();

        ClearingTransferBO clearingTransferSummaryBO = getClearingTransferBO(operationReference, username, applic, language);

        if (clearingTransferSummaryBO != null) {
            globalcommissionBoList =
                getOperationCommissionList(operationReference, clearingTransferSummaryBO.getDebitAmount(),
                                           clearingTransferSummaryBO.getCreditAmount(), clearingTransferSummaryBO.getDebitAccount(),
                                           clearingTransferSummaryBO.getCreditAccount(), username, applic, language);
        }
        clearingTrsfSummaryctx.setGlobalCommissionList(globalcommissionBoList);
        clearingTrsfSummaryctx.setClearingTransferSummaryBO(clearingTransferSummaryBO);
        return clearingTrsfSummaryctx;
    }

    public SwiftTransferSummaryContextBO getSwiftTransferSummaryContextBO(String operationReference, BigDecimal debitAmount, BigDecimal creditAmount,
                                                                          String debitAccount, String creditAccount, String username, String applic,
                                                                          String language) {
        SwiftTransferSummaryContextBO ctx = new SwiftTransferSummaryContextBO();

        List<GlobalCommissionBO> globalcommissionBoList = new ArrayList<GlobalCommissionBO>();
        SwiftTransferBO swiftTransferSummaryBO = getSwiftTransferBO(operationReference, username, applic, language);
        // In case its a Treasury transfer do not request the commission list (Treasury Transfer => TransferMode=T)
        if (swiftTransferSummaryBO != null && !"T".equalsIgnoreCase(swiftTransferSummaryBO.getTransferMode())) {
            globalcommissionBoList =
                getOperationCommissionList(operationReference, swiftTransferSummaryBO.getDebitAmount(), swiftTransferSummaryBO.getCreditAmount(),
                                           swiftTransferSummaryBO.getDebitAccount(), swiftTransferSummaryBO.getCreditAccount(), username, applic,
                                           language);
        }
        ctx.setGlobalCommissionList(globalcommissionBoList);
        ctx.setSwiftTransferSummaryBO(swiftTransferSummaryBO);
        return ctx;
    }

    private GetOperationBenefBankBicAndCountryResponse getOperationBenefBankBicAndCountry(String operationReference, String username, String applic,
                                                                                          String language) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_nooper", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlInputParameter("p_i_uname", JDBCTypes.VARCHAR_TYPE, username));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_paysiso", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_paysNom", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_bicbqbe", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_UI_UTIL_PACKAGE_NAME, "rch_benefbank_info", params, em);

        GetOperationBenefBankBicAndCountryResponse resp = new GetOperationBenefBankBicAndCountryResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        resp.setBeneficiaryBankBic((String) returnParams.get("p_o_bicbqbe"));
        resp.setBeneficiaryCountryIsoCode((String) returnParams.get("p_o_paysiso"));
        resp.setBeneficiaryCountryName((String) returnParams.get("p_o_paysNom"));
        return resp;
    }

    /**
     * Returns the list bankCodeBO
     * @param isoCodeFilter  isoCodeFilter
     * @param genericFilter genericFilter
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of bankCodeBO
     */
    public List<BankCodeBO> getBankCodeBOList(String isoCodeFilter, String genericFilter, String username, String applic, String language,
                                              String bankAbbrFilter, String bicCodeFilter, String bankCodeFilter, String bankNameFilter) {
        return paymentsTransferCommonModelSessionEJBLocal.getAdvancedBankCodeBOSearchList(isoCodeFilter, genericFilter, bankAbbrFilter, bicCodeFilter,
                                                                                          bankCodeFilter, bankNameFilter, username, applic, language);
    }

    /**
     * Returns the list bank counter codes
     * @param bankCode  bankCode
     * @param isoCodeFilter isoCodeFilter
     * @param genericFilter genericFilter
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of bank counter codes
     */
    public List<BankCounterCodeBO> getBankCounterCodeBOList(String bankCode, String isoCodeFilter, String genericFilter, String username,
                                                            String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getBankCounterCodeBOList(bankCode, isoCodeFilter, genericFilter, username, applic,
                                                                                   language);
    }

    /**
     * Returns GetRibInfoResponse
     * @param username
     * @param applic
     * @param language
     * @return
     */
    public GetRibInfoResponse getRibInfo(String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getRibInfo(username, applic, language);
    }

    /**
     * Returns CalculateAccountKeyResponses
     * @param bankCode
     * @param counterCode
     * @param account
     * @param username
     * @param applic
     * @param language
     * @return
     */
    public CalculateAccountKeyResponse calculateAccountKey(String bankCode, String counterCode, String account, String username, String applic,
                                                           String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getCalculateAccountKey(bankCode, counterCode, account, username, applic, language);
    }

    /**
     * get List of TransferCurrencyBO
     * @param userBranchCode
     * @param stateBranchCode
     * @param userActivityList
     * @param orderingPartyActivity
     * @param isoCodeFilter
     * @param bicCodeFilter
     * @param username
     * @param applic
     * @param language
     * @return
     */
    public List<TransferCurrencyBO> getTransferCurrencyBOList(String userBranchCode, String stateBranchCode, String userActivityList,
                                                              String orderingPartyActivity, String isoCodeFilter, String bicCodeFilter,
                                                              String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getTransferCurrencyBOList(userBranchCode, stateBranchCode, userActivityList,
                                                                                    orderingPartyActivity, isoCodeFilter, bicCodeFilter, username,
                                                                                    applic, language);
    }

    /**
     * Returns the list of advice languages
     * @param username username
     * @param applic applic
     * @param language language
     *
     * @return list of advice languages
     */
    public List<AdviceLanguageBO> getAdviceLanguageBOList(String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getAdviceLanguageBOList(username, applic, language);
    }

    /**
     * get list of CorrespondentBO
     * @param direction
     * @param transferCurrency
     * @param filterCodeISO
     * @param filterCodeBIC
     * @param activityCode
     * @param branch
     * @param counterCode
     * @param transferMode
     * @param username
     * @param applic
     * @param language
     * @return
     */
    public List<CorrespondentBO> getCorrespondentBOList(String directionCode, String transferCurrencyIsoCode, String ccyIsoCodeFilter,
                                                        String bicCodeFilter, String activityCode, String branchCode, String counterCode,
                                                        String transferModeCode, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getCorrespondentBOList(directionCode, transferCurrencyIsoCode, ccyIsoCodeFilter,
                                                                                 bicCodeFilter, activityCode, branchCode, counterCode,
                                                                                 transferModeCode, username, applic, language);
    }

    public List<CountryBO> getCountryBOList(String genericFilter, String isTransferFlag, String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getCountryBOList(genericFilter, isTransferFlag, username, applic, language);
    }


    public SaveClearingTransferResponse saveClearingTransfer(ClearingTransferContextBO ctx, String printerCode) {
        return saveClearingTransfer(ctx, printerCode, null);
    }

    public SaveClearingTransferResponse saveClearingTransfer(ClearingTransferContextBO ctx, String printerCode, BigDecimal swiftVersionNumber) {

        SaveClearingTransferResponse resp = new SaveClearingTransferResponse();

        if (ctx != null) {
            String benefBankCountryIso = ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars() : null;
            String benefBankCode = ctx.getBankCodeBO() != null ? ctx.getBankCodeBO().getCode() : null;
            String benefCounterCode = ctx.getBankCounterCodeBO() != null ? ctx.getBankCounterCodeBO().getCode() : null;
            String benefAccountNumber = ctx.getAccount();
            String benefAccountKey = ctx.getKey();
            String benefName = ctx.getBeneficiaryName();
            String benefAddress = ctx.getBeneficiaryAddress();
            Date operationDate = ctx.getOperationDate();
            String transferMode = ctx.getTransferModeBO().getCode();
            String periodicity = ctx.getPeriodicityBO().getCode();
            Date firstExecutionDate = ctx.getFirstExecutionDate();
            Date finalMaturityDate = ctx.getFinalMaturityDate();
            String debitAccountNumber = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getAccountNumber() : null;
            String strongCcyIsoCode = ctx.getStrongCurrencyIsoCode();
            BigDecimal exchangeRate = ctx.getExchangeRate();
            String weakCcyIsoCode = ctx.getWeakCurrencyIsoCode();
            BigDecimal debitAmount = ctx.getCounterValueAmount();
            BigDecimal creditAmount = ctx.getAmount();
            Date debitValueDate = ctx.getDebitValueDate();
            String adviveLngCode = ctx.getDebitAdviceLanguageBO() != null ? ctx.getDebitAdviceLanguageBO().getCode() : null;
            String orderingPName = ctx.getOrderingPartyName1();
            String orderingPAddress1 = ctx.getOrderingPartyAddress1();
            String orderingPAddress2 = ctx.getOrderingPartyAddress2();
            String motive = ctx.getMotive();
            String clientRelativeReference = ctx.getClientRelativeReference();
            String chequeNumber = ctx.getChequeNumber();
            String adviceDescription = ctx.getAdviceDescription();
            String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;
            String buySellCode =
                PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_DEBIT_CODE.equalsIgnoreCase(ctx.getReferenceCurrencyCode()) ? "A" : "V";

            /*String exchangeBuySellCode =
                ctx.getExchangeReferenceBO() != null ? buySellCode : null;*/
            String fixedCurrencyIsoCode = ctx.getFixedCurrencyIsoCode();
            String orderingPClientName =
                ctx.getOrderingPartyNameAndCityResponse() != null ? ctx.getOrderingPartyNameAndCityResponse().getOrderingPartyName() : null;
            String creditAccount = ctx.getCorrespondentAccount() != null ? ctx.getCorrespondentAccount().getAccountNumber() : null;
            Date creditValueDate = ctx.getCreditValueDate();

            List<GlobalCommissionBO> commissionList = ctx.getCommissionList();

            String username = ctx.getUser().getUsername();
            String applic = ctx.getTransferType().toString();
            String language = ctx.getUserLanguage();

            if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()))
                resp =
                    modifyClearingTransfer(ctx.getOperationReference(), benefBankCountryIso, benefBankCode, benefCounterCode, benefAccountNumber,
                                           benefAccountKey, benefName, benefAddress, operationDate, transferMode, periodicity, firstExecutionDate,
                                           finalMaturityDate, exchangeReference, buySellCode, debitAccountNumber, strongCcyIsoCode, exchangeRate,
                                           weakCcyIsoCode, debitAmount, creditAmount, fixedCurrencyIsoCode, debitValueDate, adviveLngCode,
                                           orderingPName, orderingPAddress1, orderingPAddress2, motive, clientRelativeReference, chequeNumber,
                                           adviceDescription, orderingPClientName, creditAccount, creditValueDate, commissionList, printerCode,
                                           swiftVersionNumber, username, applic, language,
                                           ctx.getInitialTransfer() != null ? ctx.getInitialTransfer().getImage() : null);
            else
                resp =
                    createClearingTransfer(null, ctx.getOperationNature(), benefBankCountryIso, benefBankCode, benefCounterCode, benefAccountNumber,
                                           benefAccountKey, benefName, benefAddress, ctx.getBeneficiaryInfo(), ctx.getBeneficiaryAdviceDesc(),
                                           operationDate, transferMode, periodicity, firstExecutionDate, finalMaturityDate, exchangeReference,
                                           buySellCode, debitAccountNumber, strongCcyIsoCode, exchangeRate, weakCcyIsoCode, fixedCurrencyIsoCode,
                                           ctx.getOriginAmount(), ctx.getOriginCurrencyIsoCode(), debitAmount, creditAmount, debitValueDate,
                                           adviveLngCode, orderingPName, orderingPAddress1, orderingPAddress2, motive, clientRelativeReference,
                                           chequeNumber, adviceDescription, orderingPClientName, creditAccount, creditValueDate, ctx.getSwiftDate(),
                                           commissionList, printerCode, swiftVersionNumber, ctx.getBacs(), ctx.getInitialReference(), username,
                                           applic, language);
        }
        return resp;
    }

    private SaveClearingTransferResponse modifyClearingTransfer(String operationReference, String benefBankCountryIso, String benefBankCode,
                                                                String benefCounterCode, String benefAccountNumber, String benefAccountKey,
                                                                String benefName, String benefAddress, Date operationDate, String transferMode,
                                                                String periodicity, Date firstExecutionDate, Date finalMaturityDate,
                                                                String exchangeReference, String exchangeBuySellCode, String debitAccountNumber,
                                                                String strongCcyIsoCode, BigDecimal exchangeRate, String weakCcyIsoCode,
                                                                BigDecimal debitAmount, BigDecimal creditAmount, String fixedCurrencyIsoCode,
                                                                Date debitValueDate, String adviveLngCode, String orderingPName,
                                                                String orderingPAddress1, String orderingPAddress2, String motive,
                                                                String clientRelativeReference, String chequeNumber, String adviceDescription,
                                                                String orderingPClientName, String creditAccount, Date creditValueDate,
                                                                List<GlobalCommissionBO> commissionList, String printerCode,
                                                                BigDecimal swiftVersionNumber, String username, String applic, String language,
                                                                byte[] beforeImage) {
        //Commmission Struct TODO refactor me
        List<CommissionStruct> commissionStructList = new ArrayList<CommissionStruct>();
        if (commissionList != null)
            for (int i = 0; i < commissionList.size(); i++)
                commissionStructList.add(commissionList.get(i).generateStruct());

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".ModifyClearingTransfer");

        call.addNamedArgument("p_i_OperationReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_benefBankCountryIso", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefBankCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefCounterCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAccountKey", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefName", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAddress", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OperationDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_TransferMode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Periodicity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_FirstExecutionDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_FinalMaturityDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_AccountD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_StrongCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangeRate", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_WeakCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_amountD", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_amountC", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_valueDateD", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_adviceLanguage", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPName", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPAddress1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPAddress2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Motive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_clientRelRef", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_chequeNumber", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_adviceDesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangeReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangeBuySellCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_fixedCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_named", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_accountC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_valueDateC", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_CommissionList", CommissionStructUtil.commisionCollection());
        call.addNamedArgument("p_i_printerCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.addNamedArgument("p_i_beforeImg", JDBCTypes.BLOB_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_OperationReference");
        query.addArgument("p_i_benefBankCountryIso");
        query.addArgument("p_i_BenefBankCode");
        query.addArgument("p_i_BenefCounterCode");
        query.addArgument("p_i_BenefAccount");
        query.addArgument("p_i_BenefAccountKey");
        query.addArgument("p_i_BenefName");
        query.addArgument("p_i_BenefAddress");
        query.addArgument("p_i_OperationDate");
        query.addArgument("p_i_TransferMode");
        query.addArgument("p_i_Periodicity");
        query.addArgument("p_i_FirstExecutionDate");
        query.addArgument("p_i_FinalMaturityDate");
        query.addArgument("p_i_AccountD");
        query.addArgument("p_i_StrongCurrency");
        query.addArgument("p_i_exchangeRate");
        query.addArgument("p_i_WeakCurrency");
        query.addArgument("p_i_amountD");
        query.addArgument("p_i_amountC");
        query.addArgument("p_i_valueDateD");
        query.addArgument("p_i_adviceLanguage");
        query.addArgument("p_i_OrdPName");
        query.addArgument("p_i_OrdPAddress1");
        query.addArgument("p_i_OrdPAddress2");
        query.addArgument("p_i_Motive");
        query.addArgument("p_i_clientRelRef");
        query.addArgument("p_i_chequeNumber");
        query.addArgument("p_i_adviceDesc");
        query.addArgument("p_i_exchangeReference");
        query.addArgument("p_i_exchangeBuySellCode");
        query.addArgument("p_i_fixedCurrency");
        query.addArgument("p_i_named");
        query.addArgument("p_i_accountC");
        query.addArgument("p_i_valueDateC");
        query.addArgument("p_i_CommissionList");
        query.addArgument("p_i_printerCode");
        query.addArgument("p_i_swiftVersionNumber");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_Lng");
        query.addArgument("p_i_beforeImg");

        List queryArgs = new ArrayList();
        queryArgs.add(operationReference); // p_i_nooper
        queryArgs.add(benefBankCountryIso); // p_i_benefBankCountryIso
        queryArgs.add(benefBankCode); // p_i_BenefBankCode
        queryArgs.add(benefCounterCode); // p_i_BenefCounterCode
        queryArgs.add(benefAccountNumber); // p_i_BenefAccount
        queryArgs.add(benefAccountKey); // p_i_BenefAccountKey
        queryArgs.add(benefName); // p_i_BenefName
        queryArgs.add(benefAddress); // p_i_BenefAddress
        queryArgs.add(operationDate); // p_i_OperationDate
        queryArgs.add(transferMode); // p_i_TransferMode
        queryArgs.add(periodicity); // p_i_Periodicity
        queryArgs.add(firstExecutionDate); // p_i_FirstExecutionDate
        queryArgs.add(finalMaturityDate); // p_i_FinalMaturityDate
        queryArgs.add(debitAccountNumber); // p_i_AccountD
        queryArgs.add(strongCcyIsoCode); // p_i_StrongCurrency
        queryArgs.add(exchangeRate); // p_i_exchangeRate
        queryArgs.add(weakCcyIsoCode); // p_i_WeakCurrency
        queryArgs.add(debitAmount); // p_i_amountD
        queryArgs.add(creditAmount); // p_i_amountC
        queryArgs.add(debitValueDate); // p_i_valueDateD
        queryArgs.add(adviveLngCode); // p_i_adviceLanguage
        queryArgs.add(orderingPName); // p_i_OrdPName
        queryArgs.add(orderingPAddress1); // p_i_OrdPAddress1
        queryArgs.add(orderingPAddress2); // p_i_OrdPAddress2
        queryArgs.add(motive); // p_i_Motive
        queryArgs.add(clientRelativeReference); // p_i_clientRelRef
        queryArgs.add(chequeNumber); // p_i_chequeNumber
        queryArgs.add(adviceDescription); // p_i_adviceDesc
        queryArgs.add(exchangeReference); // p_i_exchangeReference

        queryArgs.add(exchangeBuySellCode); // p_i_exchangeBuySellCode
        queryArgs.add(fixedCurrencyIsoCode); // p_i_fixedCurrency
        queryArgs.add(orderingPClientName); // p_i_named
        queryArgs.add(creditAccount); // p_i_accountC
        queryArgs.add(creditValueDate); // p_i_valueDateC
        queryArgs.add(commissionStructList); // p_i_t_CommissionList
        queryArgs.add(printerCode); // p_i_printerCode
        queryArgs.add(swiftVersionNumber);
        queryArgs.add(username); // p_i_user
        queryArgs.add(applic); // p_i_applic
        queryArgs.add(language); // p_i_Lng
        queryArgs.add(beforeImage); // p_i_beforeImg

        ServerSession serverSession = ((JpaEntityManager) em.getDelegate()).getServerSession();
        serverSession.addDescriptor(CommissionStructUtil.commissionDescriptor());

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SaveClearingTransferResponse resp = new SaveClearingTransferResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setOperationReference(operationReference);
        return resp;
    }

    private SaveClearingTransferResponse createClearingTransfer(String operationReference, String operationNature, String benefBankCountryIso,
                                                                String benefBankCode, String benefCounterCode, String benefAccountNumber,
                                                                String benefAccountKey, String benefName, String benefAddress, String beneficiaryInfo,
                                                                String beneficiaryAdviceDesc, Date operationDate, String transferMode,
                                                                String periodicity, Date firstExecutionDate, Date finalMaturityDate,
                                                                String exchangeReference, String exchangeBuySellCode, String debitAccountNumber,
                                                                String strongCcyIsoCode, BigDecimal exchangeRate, String weakCcyIsoCode,
                                                                String fixedCurrencyIsoCode, BigDecimal originAmount, String originCurrencyIsoCode,
                                                                BigDecimal debitAmount, BigDecimal creditAmount, Date debitValueDate,
                                                                String adviveLngCode, String orderingPName, String orderingPAddress1,
                                                                String orderingPAddress2, String motive, String clientRelativeReference,
                                                                String chequeNumber, String adviceDescription, String orderingPClientName,
                                                                String creditAccount, Date creditValueDate, Date swiftDate,
                                                                List<GlobalCommissionBO> commissionList, String printerCode,
                                                                BigDecimal swiftVersionNumber, String bacsnb, String initialReference,
                                                                String username, String applic, String language) {
        //Commmission Struct TODO refactor me
        List<CommissionStruct> commissionStructList = new ArrayList<CommissionStruct>();
        if (commissionList != null)
            for (int i = 0; i < commissionList.size(); i++)
                commissionStructList.add(commissionList.get(i).generateStruct());

        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".CreateClearingTransfer");

        call.addNamedArgument("p_i_forceOperationReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_operationNature", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_benefBankCountryIso", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefBankCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefCounterCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAccount", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAccountKey", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefName", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAddress", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefInfo", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_BenefAdviceDesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OperationDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_TransferMode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Periodicity", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_FirstExecutionDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_FinalMaturityDate", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_AccountD", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_StrongCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangeRate", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_WeakCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_amountD", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_amountC", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_valueDateD", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_adviceLanguage", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPName", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPAddress1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_OrdPAddress2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Motive", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_clientRelRef", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_chequeNumber", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_adviceDesc", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_exchangeReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_ExchangeBuySellCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_fixedCurrency", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_originAmnt", JDBCTypes.NUMERIC_TYPE, ModelUtils.NUMERIC_PRECISION, ModelUtils.NUMERIC_SCALE);
        call.addNamedArgument("p_i_originCur", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_swftDat", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_named", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_accountC", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_valueDateC", JDBCTypes.DATE_TYPE);
        call.addNamedArgument("p_i_t_CommissionList", CommissionStructUtil.commisionCollection());
        call.addNamedArgument("p_i_printerCode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_initReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE);
        call.addNamedArgument("p_i_bacsnb", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_Lng", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_OperationReference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_forceOperationReference");
        query.addArgument("p_i_operationNature");
        query.addArgument("p_i_benefBankCountryIso");
        query.addArgument("p_i_BenefBankCode");
        query.addArgument("p_i_BenefCounterCode");
        query.addArgument("p_i_BenefAccount");
        query.addArgument("p_i_BenefAccountKey");
        query.addArgument("p_i_BenefName");
        query.addArgument("p_i_BenefAddress");
        query.addArgument("p_i_BenefInfo");
        query.addArgument("p_i_BenefAdviceDesc");
        query.addArgument("p_i_OperationDate");
        query.addArgument("p_i_TransferMode");
        query.addArgument("p_i_Periodicity");
        query.addArgument("p_i_FirstExecutionDate");
        query.addArgument("p_i_FinalMaturityDate");
        query.addArgument("p_i_AccountD");
        query.addArgument("p_i_StrongCurrency");
        query.addArgument("p_i_exchangeRate");
        query.addArgument("p_i_WeakCurrency");
        query.addArgument("p_i_amountD");
        query.addArgument("p_i_amountC");
        query.addArgument("p_i_valueDateD");
        query.addArgument("p_i_adviceLanguage");
        query.addArgument("p_i_OrdPName");
        query.addArgument("p_i_OrdPAddress1");
        query.addArgument("p_i_OrdPAddress2");
        query.addArgument("p_i_Motive");
        query.addArgument("p_i_clientRelRef");
        query.addArgument("p_i_chequeNumber");
        query.addArgument("p_i_adviceDesc");
        query.addArgument("p_i_exchangeReference");
        query.addArgument("p_i_ExchangeBuySellCode");
        query.addArgument("p_i_fixedCurrency");
        query.addArgument("p_i_originAmnt");
        query.addArgument("p_i_originCur");
        query.addArgument("p_i_swftDat");
        query.addArgument("p_i_named");
        query.addArgument("p_i_accountC");
        query.addArgument("p_i_valueDateC");
        query.addArgument("p_i_t_CommissionList");
        query.addArgument("p_i_printerCode");
        query.addArgument("p_i_initReference");
        query.addArgument("p_i_swiftVersionNumber");
        query.addArgument("p_i_bacsnb");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_Lng");

        List queryArgs = new ArrayList();
        queryArgs.add(operationReference); // p_i_forceOperationReference
        queryArgs.add(operationNature); // p_i_operationNature
        queryArgs.add(benefBankCountryIso); // p_i_benefBankCountryIso
        queryArgs.add(benefBankCode); // p_i_BenefBankCode
        queryArgs.add(benefCounterCode); // p_i_BenefCounterCode
        queryArgs.add(benefAccountNumber); // p_i_BenefAccount
        queryArgs.add(benefAccountKey); // p_i_BenefAccountKey
        queryArgs.add(benefName); // p_i_BenefName
        queryArgs.add(benefAddress); // p_i_BenefAddress
        queryArgs.add(beneficiaryInfo); // p_i_BenefInfo
        queryArgs.add(beneficiaryAdviceDesc); // p_i_BenefAdviceDesc
        queryArgs.add(operationDate); // p_i_OperationDate
        queryArgs.add(transferMode); // p_i_TransferMode
        queryArgs.add(periodicity); // p_i_Periodicity
        queryArgs.add(firstExecutionDate); // p_i_FirstExecutionDate
        queryArgs.add(finalMaturityDate); // p_i_FinalMaturityDate
        queryArgs.add(debitAccountNumber); // p_i_AccountD
        queryArgs.add(strongCcyIsoCode); // p_i_StrongCurrency
        queryArgs.add(exchangeRate); // p_i_exchangeRate
        queryArgs.add(weakCcyIsoCode); // p_i_WeakCurrency
        queryArgs.add(debitAmount); // p_i_amountD
        queryArgs.add(creditAmount); // p_i_amountC
        queryArgs.add(debitValueDate); // p_i_valueDateD
        queryArgs.add(adviveLngCode); // p_i_adviceLanguage
        queryArgs.add(orderingPName); // p_i_OrdPName
        queryArgs.add(orderingPAddress1); // p_i_OrdPAddress1
        queryArgs.add(orderingPAddress2); // p_i_OrdPAddress2
        queryArgs.add(motive); // p_i_Motive
        queryArgs.add(clientRelativeReference); // p_i_clientRelRef
        queryArgs.add(chequeNumber); // p_i_chequeNumber
        queryArgs.add(adviceDescription); // p_i_adviceDesc
        queryArgs.add(exchangeReference); // p_i_exchangeReference

        queryArgs.add(exchangeBuySellCode); // p_i_ExchangeBuySellCode
        queryArgs.add(fixedCurrencyIsoCode); // p_i_fixedCurrency
        queryArgs.add(originAmount); // p_i_originAmnt
        queryArgs.add(originCurrencyIsoCode); // p_i_originCur
        queryArgs.add(swiftDate); // p_i_swftDat
        queryArgs.add(orderingPClientName); // p_i_named
        queryArgs.add(creditAccount); // p_i_accountC
        queryArgs.add(creditValueDate); // p_i_valueDateC
        queryArgs.add(commissionStructList); // p_i_t_CommissionList
        queryArgs.add(printerCode); // p_i_printerCode
        queryArgs.add(initialReference); // p_i_initReference
        queryArgs.add(swiftVersionNumber);
        queryArgs.add(bacsnb);
        queryArgs.add(username); // p_i_user
        queryArgs.add(applic); // p_i_applic
        queryArgs.add(language); // p_i_Lng

        ServerSession serverSession = ((JpaEntityManager) em.getDelegate()).getServerSession();
        serverSession.addDescriptor(CommissionStructUtil.commissionDescriptor());

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SaveClearingTransferResponse resp = new SaveClearingTransferResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setOperationReference((String) record.get("p_o_OperationReference"));
        return resp;
    }


    private SaveSwiftTransferResponse modifySwiftTransfer(String operationReference, String benefBankBic
                                                          //benef
                                                          , String mt103Flag, String ibanControl, String residenceCountryIso, String benefName,
                                                          String benefAddress, String benefIban, String benefBic, String benefBicName,
                                                          String benefCountryIso, String benefBankAccount, String benefBankBranch,
                                                          String infoToBenefBank, String benefCorrespBankBic, String benefCorrespBankName,
                                                          String benefCorrespAccount, String itrmdBankBic, String itrmdBankName, String benefKeySwift,
                                                          String benefCommCode, String benefBankKeySwift, String benefBankCommCode,
                                                          String benefCorrespKeySwift, String benefCorrespCommCode, String intermediaryBankCommCode
                                                          //ordering Party
                                                          , String periodicityCode, String transferModeCode, Date operationDate,
                                                          BigDecimal debitAmount, BigDecimal creditAmount, String strongCcyIsoCode,
                                                          String weakCcyIsoCode, BigDecimal exchangeRate, Date nextExecutionDate,
                                                          Date finalMaturityDate, String debitAccountNumber, String orderingPName1,
                                                          String orderingPName2, String orderingPAddress1, String orderingPAddress2,
                                                          String orderingPPostalCode, String orderingPAdviceLng, String orderingPInstitution,
                                                          String orderingPOnSwiftFlag, Date debitValueDate, String motive, String clientRelativeRef,
                                                          String chequeNumber, String orderingPReference, String debitClientName,
                                                          String debitAdviceTypeCode, String orderingPCityName, String orderingPartyBic,
                                                          String orderingPartyKeySwift, String orderingPartyCommCode, String account50k,
                                                          //Correspondent
                                                          Date creditValueDate, String bdlZoneCode, String creditAccountNumber, String bankOperCode,
                                                          String instructionCode, String infoToCorresp, String chargesCode, String information23,
                                                          String correspondentBicCode, String swiftComTypeCode, String ourAccountNumber,
                                                          String ourCorrespKeySwift, String ourCorrespCommCode, String creditAccountType,
                                                          String creditClientName, String correspCityName,

                                                          //payRep
                                                          String payRepEconomicCode, String payRepResidenceTypeCode, String payRepCountryIsoCode,
                                                          String payRepSirenCode, String payRepBankCode, String payRepBranchCode, String payRepCode,
                                                          //Commissions
                                                          List<GlobalCommissionBO> commissionList, String exchangeReference,
                                                          String exchangeBuySellCode, String fixedCcy, Date cutoffDate, String printerCode,
                                                          BigDecimal swiftVersionNumber, String username, String applic, String language,
                                                          byte[] beforeImage) {
        //Commmission Struct TODO refactor me
        List<CommissionStruct> commissionStructList = new ArrayList<CommissionStruct>();
        if (commissionList != null)
            for (int i = 0; i < commissionList.size(); i++)
                commissionStructList.add(commissionList.get(i).generateStruct());

        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_reference", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlInputParameter("p_i_benefBankBic", JDBCTypes.VARCHAR_TYPE, benefBankBic));
        params.add(new PlsqlInputParameter("p_i_mt103Flag", JDBCTypes.VARCHAR_TYPE, mt103Flag));
        params.add(new PlsqlInputParameter("p_i_ibanControl", JDBCTypes.VARCHAR_TYPE, ibanControl));
        params.add(new PlsqlInputParameter("p_i_residenceCountryIso", JDBCTypes.VARCHAR_TYPE, residenceCountryIso));
        params.add(new PlsqlInputParameter("p_i_benefName", JDBCTypes.VARCHAR_TYPE, benefName));
        params.add(new PlsqlInputParameter("p_i_benefAddress", JDBCTypes.VARCHAR_TYPE, benefAddress));
        params.add(new PlsqlInputParameter("p_i_benefIban", JDBCTypes.VARCHAR_TYPE, benefIban));
        params.add(new PlsqlInputParameter("p_i_benefBic", JDBCTypes.VARCHAR_TYPE, benefBic));
        params.add(new PlsqlInputParameter("p_i_benefBicName", JDBCTypes.VARCHAR_TYPE, benefBicName));
        params.add(new PlsqlInputParameter("p_i_benefCountryIso", JDBCTypes.VARCHAR_TYPE, benefCountryIso));
        params.add(new PlsqlInputParameter("p_i_benefBankAccount", JDBCTypes.VARCHAR_TYPE, benefBankAccount));
        params.add(new PlsqlInputParameter("p_i_benefBankBranch", JDBCTypes.VARCHAR_TYPE, benefBankBranch));
        params.add(new PlsqlInputParameter("p_i_infoToBenefBank", JDBCTypes.VARCHAR_TYPE, infoToBenefBank));
        params.add(new PlsqlInputParameter("p_i_benefCorrespBankBic", JDBCTypes.VARCHAR_TYPE, benefCorrespBankBic));
        params.add(new PlsqlInputParameter("p_i_benefCorrespBankName", JDBCTypes.VARCHAR_TYPE, benefCorrespBankName));
        params.add(new PlsqlInputParameter("p_i_benefCorrespAccount", JDBCTypes.VARCHAR_TYPE, benefCorrespAccount));
        params.add(new PlsqlInputParameter("p_i_itrmdBankBic", JDBCTypes.VARCHAR_TYPE, itrmdBankBic));
        params.add(new PlsqlInputParameter("p_i_itrmdBankName", JDBCTypes.VARCHAR_TYPE, itrmdBankName));
        params.add(new PlsqlInputParameter("p_i_benefKeySwift", JDBCTypes.VARCHAR_TYPE, benefKeySwift));
        params.add(new PlsqlInputParameter("p_i_benefCommCode", JDBCTypes.VARCHAR_TYPE, benefCommCode));
        params.add(new PlsqlInputParameter("p_i_benefBankKeySwift", JDBCTypes.VARCHAR_TYPE, benefBankKeySwift));
        params.add(new PlsqlInputParameter("p_i_benefBankCommCode", JDBCTypes.VARCHAR_TYPE, benefBankCommCode));
        params.add(new PlsqlInputParameter("p_i_benefCorrespKeySwift", JDBCTypes.VARCHAR_TYPE, benefCorrespKeySwift));
        params.add(new PlsqlInputParameter("p_i_benefCorrespCommCode", JDBCTypes.VARCHAR_TYPE, benefCorrespCommCode));
        params.add(new PlsqlInputParameter("p_i_intermediaryBankCommCode", JDBCTypes.VARCHAR_TYPE, intermediaryBankCommCode));
        params.add(new PlsqlInputParameter("p_i_periodicity", JDBCTypes.VARCHAR_TYPE, periodicityCode));
        params.add(new PlsqlInputParameter("p_i_trsfMode", JDBCTypes.VARCHAR_TYPE, transferModeCode));
        params.add(new PlsqlInputParameter("p_i_operationDate", JDBCTypes.DATE_TYPE, operationDate));
        params.add(new PlsqlInputParameter("p_i_debitAmount", JDBCTypes.NUMERIC_TYPE, debitAmount));
        params.add(new PlsqlInputParameter("p_i_creditAmount", JDBCTypes.NUMERIC_TYPE, creditAmount));
        params.add(new PlsqlInputParameter("p_i_strongCcy", JDBCTypes.VARCHAR_TYPE, strongCcyIsoCode));
        params.add(new PlsqlInputParameter("p_i_weakCcy", JDBCTypes.VARCHAR_TYPE, weakCcyIsoCode));
        params.add(new PlsqlInputParameter("p_i_exchangeRate", JDBCTypes.NUMERIC_TYPE, exchangeRate));
        params.add(new PlsqlInputParameter("p_i_firstExecDate", JDBCTypes.DATE_TYPE, nextExecutionDate));
        params.add(new PlsqlInputParameter("p_i_maturityDate", JDBCTypes.DATE_TYPE, finalMaturityDate));
        params.add(new PlsqlInputParameter("p_i_accountd", JDBCTypes.VARCHAR_TYPE, debitAccountNumber));
        params.add(new PlsqlInputParameter("p_i_ordPName1", JDBCTypes.VARCHAR_TYPE, orderingPName1));
        params.add(new PlsqlInputParameter("p_i_ordPName2", JDBCTypes.VARCHAR_TYPE, orderingPName2));
        params.add(new PlsqlInputParameter("p_i_ordPAddress1", JDBCTypes.VARCHAR_TYPE, orderingPAddress1));
        params.add(new PlsqlInputParameter("p_i_ordPAddress2", JDBCTypes.VARCHAR_TYPE, orderingPAddress2));
        params.add(new PlsqlInputParameter("p_i_ordPPostCode", JDBCTypes.VARCHAR_TYPE, orderingPPostalCode));
        params.add(new PlsqlInputParameter("p_i_ordPAdviceLng", JDBCTypes.VARCHAR_TYPE, orderingPAdviceLng));
        params.add(new PlsqlInputParameter("p_i_ordPInstitution", JDBCTypes.VARCHAR_TYPE, orderingPInstitution));
        params.add(new PlsqlInputParameter("p_i_ordPOnSwiftFlag", JDBCTypes.VARCHAR_TYPE, orderingPOnSwiftFlag));
        params.add(new PlsqlInputParameter("p_i_valueDateD", JDBCTypes.DATE_TYPE, debitValueDate));
        params.add(new PlsqlInputParameter("p_i_motive", JDBCTypes.VARCHAR_TYPE, motive));
        params.add(new PlsqlInputParameter("p_i_clientRelRef", JDBCTypes.VARCHAR_TYPE, clientRelativeRef));
        params.add(new PlsqlInputParameter("p_i_chequeNumber", JDBCTypes.VARCHAR_TYPE, chequeNumber));
        params.add(new PlsqlInputParameter("p_i_ordPReference", JDBCTypes.VARCHAR_TYPE, orderingPReference));
        params.add(new PlsqlInputParameter("p_i_ordPBic", JDBCTypes.VARCHAR_TYPE, orderingPartyBic));
        params.add(new PlsqlInputParameter("p_i_ordPKeySwift", JDBCTypes.VARCHAR_TYPE, orderingPartyKeySwift));
        params.add(new PlsqlInputParameter("p_i_ordPCommCode", JDBCTypes.VARCHAR_TYPE, orderingPartyCommCode));
        params.add(new PlsqlInputParameter("p_i_Account50k", JDBCTypes.VARCHAR_TYPE, account50k));
        params.add(new PlsqlInputParameter("p_i_named", JDBCTypes.VARCHAR_TYPE, debitClientName));
        params.add(new PlsqlInputParameter("p_i_adviceTypeD", JDBCTypes.VARCHAR_TYPE, debitAdviceTypeCode));
        params.add(new PlsqlInputParameter("p_i_ordPCity", JDBCTypes.VARCHAR_TYPE, orderingPCityName));
        params.add(new PlsqlInputParameter("p_i_valueDateC", JDBCTypes.DATE_TYPE, creditValueDate));
        params.add(new PlsqlInputParameter("p_i_bdlZone", JDBCTypes.VARCHAR_TYPE, bdlZoneCode));
        params.add(new PlsqlInputParameter("p_i_accountC", JDBCTypes.VARCHAR_TYPE, creditAccountNumber));
        params.add(new PlsqlInputParameter("p_i_bankOperCode", JDBCTypes.VARCHAR_TYPE, bankOperCode));
        params.add(new PlsqlInputParameter("p_i_instructionCode", JDBCTypes.VARCHAR_TYPE, instructionCode));
        params.add(new PlsqlInputParameter("p_i_infoToCorresp", JDBCTypes.VARCHAR_TYPE, infoToCorresp));
        params.add(new PlsqlInputParameter("p_i_charges", JDBCTypes.VARCHAR_TYPE, chargesCode));
        params.add(new PlsqlInputParameter("p_i_info23", JDBCTypes.VARCHAR_TYPE, information23));
        params.add(new PlsqlInputParameter("p_i_ourCorrespBic", JDBCTypes.VARCHAR_TYPE, correspondentBicCode));
        params.add(new PlsqlInputParameter("p_i_swiftCommType", JDBCTypes.VARCHAR_TYPE, swiftComTypeCode));
        params.add(new PlsqlInputParameter("p_i_ourAccount", JDBCTypes.VARCHAR_TYPE, ourAccountNumber));
        params.add(new PlsqlInputParameter("p_i_ourCorrespKeySwift", JDBCTypes.VARCHAR_TYPE, ourCorrespKeySwift));
        params.add(new PlsqlInputParameter("p_i_ourCorrespCommCode", JDBCTypes.VARCHAR_TYPE, ourCorrespCommCode));
        params.add(new PlsqlInputParameter("p_i_accountcType", JDBCTypes.VARCHAR_TYPE, creditAccountType));
        params.add(new PlsqlInputParameter("p_i_namec", JDBCTypes.VARCHAR_TYPE, creditClientName));
        params.add(new PlsqlInputParameter("p_i_correspCity", JDBCTypes.VARCHAR_TYPE, correspCityName));
        params.add(new PlsqlInputParameter("p_i_PayRepEcoCode", JDBCTypes.VARCHAR_TYPE, payRepEconomicCode));
        params.add(new PlsqlInputParameter("p_i_PayRepResidence", JDBCTypes.VARCHAR_TYPE, payRepResidenceTypeCode));
        params.add(new PlsqlInputParameter("p_i_PayRepCountryIso", JDBCTypes.VARCHAR_TYPE, payRepCountryIsoCode));
        params.add(new PlsqlInputParameter("p_i_PayRepSiren", JDBCTypes.VARCHAR_TYPE, payRepSirenCode));
        params.add(new PlsqlInputParameter("p_i_PayRepBankCode", JDBCTypes.VARCHAR_TYPE, payRepBankCode));
        params.add(new PlsqlInputParameter("p_i_PayRepBranchCode", JDBCTypes.VARCHAR_TYPE, payRepBranchCode));
        params.add(new PlsqlInputParameter("p_i_PayRepCode", JDBCTypes.VARCHAR_TYPE, payRepCode));
        params.add(new PlsqlInputParameter("p_i_CommissionList", CommissionStructUtil.commisionCollection(), commissionStructList));
        params.add(new PlsqlInputParameter("p_i_exchangeReference", JDBCTypes.VARCHAR_TYPE, exchangeReference));

        params.add(new PlsqlInputParameter("p_i_exchangeBuySellCode", JDBCTypes.VARCHAR_TYPE, exchangeBuySellCode));
        params.add(new PlsqlInputParameter("p_i_fixedCcy", JDBCTypes.VARCHAR_TYPE, fixedCcy));
        params.add(new PlsqlInputParameter("p_i_cutoffDate", JDBCTypes.DATE_TYPE, cutoffDate));
        params.add(new PlsqlInputParameter("p_i_printerCode", JDBCTypes.VARCHAR_TYPE, printerCode));
        params.add(new PlsqlInputParameter("p_i_swiftVersionNumber", JDBCTypes.NUMERIC_TYPE, swiftVersionNumber));
        params.add(new PlsqlInputParameter("p_i_beforeImg", JDBCTypes.BLOB_TYPE, beforeImage));
        params.add(new PlsqlInputParameter("p_i_user", JDBCTypes.VARCHAR_TYPE, username));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        List<ObjectRelationalDataTypeDescriptor> descriptorList = new ArrayList<ObjectRelationalDataTypeDescriptor>();
        descriptorList.add(CommissionStructUtil.commissionDescriptor());

        HashMap<String, Object> returnParams =
            QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "modifySwiftTransfer", params, descriptorList, em);

        SaveSwiftTransferResponse resp = new SaveSwiftTransferResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        resp.setOperationReference(operationReference);
        return resp;
    }

    public SaveSwiftTransferResponse saveSwiftTransfer(SwiftTransferContextBO ctx, String printerCode) {
        return saveSwiftTransfer(ctx, printerCode, null);
    }

    public SaveSwiftTransferResponse saveSwiftTransfer(SwiftTransferContextBO ctx, String printerCode, BigDecimal swiftVersionNumber) {
        SaveSwiftTransferResponse resp = new SaveSwiftTransferResponse();

        if (ctx != null && PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {

            String paymentEconomicCode = null;
            String paymentReportingCode = null;
            String paymentResidence = null;
            String paymentCountry = null;
            String paymentAccountType = null;
            if (ctx.getPaymentReportingContextBO() != null) {
                paymentEconomicCode =
                    ctx.getPaymentReportingContextBO().getPaymentEconomicCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentEconomicCodeBO().getCode() : null;
                paymentReportingCode =
                    ctx.getPaymentReportingContextBO().getPaymentReportingCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentReportingCodeBO().getCode() : null;
                paymentResidence =
                    ctx.getPaymentReportingContextBO().getPaymentResidenceTypeCodeBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentResidenceTypeCodeBO().getCode() : null;
                paymentCountry =
                    ctx.getPaymentReportingContextBO().getPaymentCountryBO() != null ?
                    ctx.getPaymentReportingContextBO().getPaymentCountryBO().getIsoCode2Chars() : null;
                paymentAccountType = ctx.getPaymentReportingContextBO().getPaymentAccountTypeCode();
            }

            String benefCommCode = !StringUtils.isEmpty(ctx.getBenefSendSwiftFlag()) ? ctx.getBenefSendSwiftFlag() : "C";
            String benefKeySwift = "S".equalsIgnoreCase(ctx.getBenefSendBicFlag()) ? ctx.getBenefSendBicFlag() : null; // should be either S or null

            String benefBankCommCode = !StringUtils.isEmpty(ctx.getBenefBankSendSwiftFlag()) ? ctx.getBenefBankSendSwiftFlag() : "C";
            String benefBankKeySwift =
                "S".equalsIgnoreCase(ctx.getBenefBankSendBicFlag()) ? ctx.getBenefBankSendBicFlag() : null; // should be either S or null

            String benefCorrespCommCode = !StringUtils.isEmpty(ctx.getBenefCorrespBankSendSwiftFlag()) ? ctx.getBenefCorrespBankSendSwiftFlag() : "C";
            String benefCorrespKeySwift =
                "S".equalsIgnoreCase(ctx.getBenefCorrespBankSendBicFlag()) ? ctx.getBenefCorrespBankSendBicFlag() :
                null; // should be either S or null

            String intermediaryBankCommCode =
                !StringUtils.isEmpty(ctx.getIntermediaryBankSendBicFlag()) ? ctx.getIntermediaryBankSendBicFlag() : "C"; // S or C

            String orderingPartyCommCode = !StringUtils.isEmpty(ctx.getOrderingSendSwiftFlag()) ? ctx.getOrderingSendSwiftFlag() : "C";
            String orderingPartyKeySwift =
                "S".equalsIgnoreCase(ctx.getOrderingSendBicFlag()) ? ctx.getOrderingSendBicFlag() : null; // should be either S or null

            String ourCorrespCommCode = !StringUtils.isEmpty(ctx.getOurCorrespBankSendSwiftFlag()) ? ctx.getOurCorrespBankSendSwiftFlag() : "C";
            String ourCorrespKeySwift =
                "S".equalsIgnoreCase(ctx.getOurCorrespBankSendBicFlag()) ? ctx.getOurCorrespBankSendBicFlag() : null; // should be either S or null

            String orderingPartyName1 = !StringUtils.isEmpty(ctx.getOrderingPartyName1()) ? ctx.getOrderingPartyName1().toUpperCase().trim() : null;

            String orderingPartyName2 =
                !StringUtils.isEmpty(ctx.getOrderingPartyName2()) ?
                StringUtils.substringSafelyToLength(ctx.getOrderingPartyName2().toUpperCase().trim(), 0, 35) : null;

            String orderingPartyAddress1 = !StringUtils.isEmpty(ctx.getOrderingPartyAddress1()) ? ctx.getOrderingPartyAddress1().trim() : null;
            String orderingPartyAddress2 = !StringUtils.isEmpty(ctx.getOrderingPartyAddress2()) ? ctx.getOrderingPartyAddress2().trim() : null;
            String clientRelativeReference =
                !StringUtils.isEmpty(ctx.getClientRelativeReference()) ? ctx.getClientRelativeReference().toUpperCase().trim() : null;
            String motive = !StringUtils.isEmpty(ctx.getMotive()) ? ctx.getMotive().trim() : null;
            String infoToCorrespondentText =
                ctx.getInfoToCorrespondent() != null ?
                StringUtils.concatenate(ctx.getInfoToCorrespondent().getAbbreviation(),
                                        !StringUtils.isEmpty(ctx.getInfoToCorrespondentText()) ? ctx.getInfoToCorrespondentText().trim() : "") : null;
            String information23 = !StringUtils.isEmpty(ctx.getInformation23()) ? ctx.getInformation23().trim() : null;
            String account50k = !StringUtils.isEmpty(ctx.getAccount50k()) ? ctx.getAccount50k().trim() : null;
            String orderingPartyPostalCode =
                !StringUtils.isEmpty(ctx.getOrderingPartyPostalCode()) ? ctx.getOrderingPartyPostalCode().toUpperCase().trim() : null;
            String orderingPartyReference = !StringUtils.isEmpty(ctx.getOrderingPartyReference()) ? ctx.getOrderingPartyReference().trim() : null;
            String beneficiaryAddress = !StringUtils.isEmpty(ctx.getBeneficiaryAddress()) ? ctx.getBeneficiaryAddress().toUpperCase().trim() : null;
            String beneficiaryName = !StringUtils.isEmpty(ctx.getBeneficiaryName()) ? ctx.getBeneficiaryName().toUpperCase().trim() : null;
            String beneficiaryIban = !StringUtils.isEmpty(ctx.getBeneficiaryIban()) ? ctx.getBeneficiaryIban().trim() : null;
            String beneficiaryBankBranch = !StringUtils.isEmpty(ctx.getBeneficiaryBankBranch()) ? ctx.getBeneficiaryBankBranch().trim() : null;
            String infoToBeneficiaryBankText =
                ctx.getInfoToBeneficiaryBankSwiftAbr() != null ?
                StringUtils.concatenate(ctx.getInfoToBeneficiaryBankSwiftAbr().getAbbreviation(),
                                        !StringUtils.isEmpty(ctx.getInfoToBeneficiaryBankText()) ? ctx.getInfoToBeneficiaryBankText().trim() : "") :
                null;
            //            String beneficiaryBankAccount =
            //                !StringUtils.isEmpty(ctx.getBeneficiaryBankAccount()) ?
            //                ctx.getBeneficiaryBankAccount().toUpperCase().trim() : null;
            String account = !StringUtils.isEmpty(ctx.getBeneficiaryBankAccount()) ? ctx.getBeneficiaryBankAccount().trim() : "";
            String beneficiaryBankAccount =
                ctx.getBeneficiaryBankAccountSwiftAbr() != null ?
                StringUtils.concatenate(ctx.getBeneficiaryBankAccountSwiftAbr().getAbbreviation(), account) : account;

            String beneficiaryCorrespondentAccount =
                !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentAccount()) ? ctx.getBeneficiaryCorrespondentAccount().trim() : null;
            String beneficiaryCorrespondentBankName =
                !StringUtils.isEmpty(ctx.getBeneficiaryCorrespondentBankName()) ? ctx.getBeneficiaryCorrespondentBankName().trim() : null;
            String intermediaryBankName = !StringUtils.isEmpty(ctx.getIntermediaryBankName()) ? ctx.getIntermediaryBankName().trim() : null;
            String beneficiaryBankName =
                !StringUtils.isEmpty(ctx.getBeneficiaryBankName()) ? ctx.getBeneficiaryBankName().toUpperCase().trim() : null;
            String buySellCode =
                PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_DEBIT_CODE.equalsIgnoreCase(ctx.getReferenceCurrencyCode()) ? "A" : "V";
            resp =
                modifySwiftTransfer(ctx.getOperationReference(), ctx.getBeneficiaryBankBic() != null ? ctx.getBeneficiaryBankBic().getBicCode() : null
                                    //benef
                                    , ctx.isIsMT103() ? "O" : "N", ctx.getIbanControl(), ctx.getResidenceCountryIso(), beneficiaryName,
                                    beneficiaryAddress, beneficiaryIban, ctx.getBeneficiaryBic() != null ? ctx.getBeneficiaryBic().getBic() : null,
                                    beneficiaryBankName,
                                    ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getIsoCode2Chars() : null,
                                    beneficiaryBankAccount, beneficiaryBankBranch, infoToBeneficiaryBankText,
                                    ctx.getBeneficiaryCorrespondentBic() != null ? ctx.getBeneficiaryCorrespondentBic().getBic() : null,
                                    beneficiaryCorrespondentBankName, beneficiaryCorrespondentAccount,
                                    ctx.getIntermediaryBic() != null ? ctx.getIntermediaryBic().getBic() : null, intermediaryBankName, benefKeySwift,
                                    benefCommCode, benefBankKeySwift, benefBankCommCode, benefCorrespKeySwift, benefCorrespCommCode, intermediaryBankCommCode,

                                    //ordering Party
                                    ctx.getSwiftPeriodicity().getCode(), ctx.getTransferMode().getCode(), ctx.getOperationDate(),
                                    ctx.getCounterValueAmount(), ctx.getAmount(), ctx.getStrongCurrencyIsoCode(), ctx.getWeakCurrencyIsoCode(),
                                    ctx.getExchangeRate(), ctx.getFirstExecutionDate(), ctx.getFinalMaturityDate(),
                                    ctx.getDebitAccount().getAccountNumber(), orderingPartyName1, orderingPartyName2, orderingPartyAddress1,
                                    orderingPartyAddress2, orderingPartyPostalCode,
                                    ctx.getSwiftAdviceLanguage() != null ? ctx.getSwiftAdviceLanguage().getCode() : null,
                                    ctx.getOrderingInstitution(), "Y".equals(ctx.getOrderingPartyOnSwift()) ? "O" : "N", ctx.getDebitValueDate(),
                                    motive, clientRelativeReference, ctx.getChequeNumber(), orderingPartyReference,
                                    ctx.getOrderingPartyNameAndCityResponse() != null ?
                                    ctx.getOrderingPartyNameAndCityResponse().getOrderingPartyName() : null,
                                    ctx.getDefaultAdviceTypeResponse() != null ? ctx.getDefaultAdviceTypeResponse().getAdviceGenerationType() : null,
                                    ctx.getOrderingPartyNameAndCityResponse() != null ? ctx.getOrderingPartyNameAndCityResponse().getCityName() :
                                    null, ctx.getOrderingPartyBic(), orderingPartyKeySwift, orderingPartyCommCode, account50k,

                                    //Correspondent
                                    ctx.getCreditValueDate(), ctx.getBdlZone() != null ? ctx.getBdlZone().getCode() : null,
                                    ctx.getCorrespondent().getAccount(),
                                    ctx.getBankOperationCode() != null ?
                                    StringUtils.substringSafely(ctx.getBankOperationCode().getAbbreviation(), 2, 6) : null,
                                    ctx.getInstructionCode() != null ? StringUtils.substringSafely(ctx.getInstructionCode().getAbbreviation(), 2, 6) :
                                    null, infoToCorrespondentText, ctx.getSwiftCharge() != null ? ctx.getSwiftCharge().getCode() : null,
                                    information23, ctx.getCorrespondantBic(), ctx.getSwiftCommissionTypeCode(), ctx.getOurAccount(),
                                    ourCorrespKeySwift, ourCorrespCommCode, paymentAccountType /*ctx.getCorrespondentAccountTypeCode()*/,
                                    ctx.getCorrespondentNameAndCityResponse() != null ?
                                    ctx.getCorrespondentNameAndCityResponse().getOrderingPartyName() : null,
                                    ctx.getCorrespondentNameAndCityResponse() != null ? ctx.getCorrespondentNameAndCityResponse().getCityName() : null

                                    //payRep
                                    ,

                                    paymentEconomicCode, paymentResidence, paymentCountry, null, null, null, paymentReportingCode,
                                    //Commissions
                                    ctx.getCommissionList(),
                                    ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null,
                                    /*ctx.getExchangeReferenceBO() != null ?
                                    ctx.getExchangeReferenceBO().getBuySellCode() : null*/buySellCode, ctx.getFixedCurrencyIsoCode(),
                ctx.getCutoffDate(), printerCode, swiftVersionNumber, ctx.getLegacyUser().getUname(), ctx.getTransferType().toString(),
                ctx.getUserLanguage(), ctx.getInitialTransfer().getImage());
        }
        return resp;
    }

    public CheckTransferTypeEnabledResponse checkTransferTypeEnabled(String applic) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".CheckCapitalBnkTypeVir");
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_applic");
        List queryArgs = new ArrayList();
        queryArgs.add(applic);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        CheckTransferTypeEnabledResponse resp = new CheckTransferTypeEnabledResponse();
        resp.setFnReturn("N");
        resp.setTransferTypeEnabledFlag((String) record.get("RESULT"));
        // TODO : CHECK WITH ELIOS (HAVE NOT eRROR tEXT)
        return resp;
    }

    public CheckAmountLedgerLimitResponse checkamountledgerlimit(BigDecimal amount, String ledgerCode, String currency, BigDecimal availableBalance) {
        return paymentsTransferCommonModelSessionEJBLocal.checkamountledgerlimit(amount, ledgerCode, currency, availableBalance);
    }

    /**
     * Function to return the cutoff date
     * @param countryIsoCode
     * @param currencyIsoCode
     * @return Standard Core PLSQL response
     */
    public GetCutoffDateResponse getCutoffDate(String countryIsoCode, String currencyIsoCode, String applic) {
        return paymentsTransferCommonModelSessionEJBLocal.getCutoffDate(countryIsoCode, currencyIsoCode, applic);
    }

    /**
     * Set the core banking language
     * @param language
     */
    private void setCoreBankingLanguage(String language) {
        commonGenericCommonModelSessionEJBLocal.setCoreBankingLanguage(language);
    }

    private ControlChequeRefForModeQResponse controlChequeRefForModeQ(String accountNumber, String chequeRef, String operationReference,
                                                                      String applic) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_UI_UTIL_PACKAGE_NAME + ".p_check_controls_q");

        call.addNamedArgument("p_i_account", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_chqreference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_operreference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_account");
        query.addArgument("p_i_chqreference");
        query.addArgument("p_i_operreference");
        query.addArgument("p_i_applic");

        List queryArgs = new ArrayList();
        queryArgs.add(accountNumber);
        queryArgs.add(chequeRef);
        queryArgs.add(operationReference);
        queryArgs.add(applic);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        ControlChequeRefForModeQResponse resp = new ControlChequeRefForModeQResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    public ClearingTransferBO getClearingTransferBO(String operationReference, String username, String applic, String language) {
        String sql =
            "SELECT periodicity, beneficiaryBankCountry, beneficiaryBankName, transferCurrency, amount, cvamount, " +
            "correspondentAccount, correspondentName, correspondentCurrency, CRvalueDate, netAmountDebited, " +
            "netAmountTransfered, bacsnumber, transferCurrency, amount, cvamount, correspondentAccount, " +
            "correspondentName, correspondentCurrency, CRvalueDate, netAmountDebited, netAmountTransfered, bacsNumber, " +
            "operationReference, beneficiaryBankCode, beneficiaryBankCounterCode, beneficiaryAccount, benefAccountKey, " +
            "beneficiaryName, beneficiaryAddress1, beneficiaryAddress2, operationDate, transferMode, periodicityCode, " +
            "nextExecutionDate, finalMaturityDate, debitAccount, strongCurrencyCode, exchangeRate, weakCurrencyCode, " +
            "creditAmount, debitAmount, orderingPartyAdviceLanguage, orderingPartyName, orderingPartyAddress1, " +
            "orderingPartyAddress2, motive1, motive2, motive3, orderingPartyAdviceDesc1, orderingPartyAdviceDesc2, " +
            "exchangeReference, creditCurrencyCode, debitValueDate, relativeReference, chequeNumber, creditValueDate, " +
            "creditAccount, creditClient, debitClient, image, debitAccountLedger, debitAccountLedgerDesc, creditAccountLedger, " +
            "creditAccountLedgerDesc, debitCurrencyCode, buySellCode FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME +
            ".getClearingTrsfDetails(?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(operationReference);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<ClearingTransferBO> clearingTransfrBOList = QueryUtils.executeNativeQuery(sql, ClearingTransferBO.class, params, em);
        return clearingTransfrBOList != null && clearingTransfrBOList.size() > 0 ? clearingTransfrBOList.get(0) : null;

    }

    public SepaTransferBO getSepaTransferBO(String operationReference, String username, String applic, String language) {
        String sql =
            "SELECT beneficiaryBankCountry, beneficiaryBankBic, beneficiaryBankName, intermidiaryBankBic, " +
            "intermediaryBankName, firstExecutionDate, transferCurrency, operationPosition, operationPositionDesc, " +
            "debitDateValeur,relativeReference relativeRefClient, periodicityCode, periodicityDesc, correspondentAccount, " +
            "correspondentName, correspondentCurrency,creditValueDate CRvalueDate, netAmountDebited, netAmountTransfered, " +
            "operationReference, BeneficiaryIban, beneficiaryName, beneficiaryAddress1, beneficiaryAddress2, " +
            "beneficiaryAddress3, operationDate, transferMode, transferModeDesc, nextExecutionDate, finalMaturityDate, " +
            "debitClient, debitClientName, debitAccount, strongCurrencyCode, exchangeRate, weakCurrencyCode, creditAmount, " +
            "debitAmount, debitCurrencyCode, orderingPartyAdviceLanguage, orderingPartyName, orderingPartyAddress1, " +
            "orderingPartyAddress2, motive1, motive2, orderingPartyAdviceDesc1, orderingPartyAdviceDesc2, " +
            "exchangeReference, creditCurrencyCode, debitValueDate, creditValueDate, debitAccountCurrency, " +
            "creditAccount, creditClient, relativeReference, beneficiaryOriginatorId, orderingPartyOriginatorId, " +
            "adviceLanguageDescription, orderingPartyIban, image, cutoffDate, debitAccountLedger, debitAccountLedgerDesc, " +
            "corAccountLedger, corAccountLedgerDesc, buySellCode FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getSepaTrsfDetails(?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(operationReference);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SepaTransferBO> sepaTransfrBOList = QueryUtils.executeNativeQuery(sql, SepaTransferBO.class, params, em);
        return sepaTransfrBOList != null && sepaTransfrBOList.size() > 0 ? sepaTransfrBOList.get(0) : new SepaTransferBO();
    }

    public SwiftTransferBO getSwiftTransferBO(String operationReference, String username, String applic, String language) {
        String sql =
            "SELECT periodicity, netAmountDebited, netAmountTransfered, bank_name, benef_bank_country, " +
            "corres_bank_name, correspondent_name, operationReference, operationDate, creditAmount, debitAmount," +
            "debitCurrencyCode, creditCurrencyCode, strongCurrencyCode, weakCurrencyCode, exchangeReference, " +
            "beneficiaryBankName, exchangeRate, nextExecutionDate, finalMaturityDate, debitClient, debitAccount, " +
            "orderingPartyName, orderingPartyName2, orderingPartyAddress1, orderingPartyAddress2, " +
            "orderingPartyPostalCode, motive1, motive2, debitValueDate, relativeReference, chequeNumber, " +
            "orderingPartyReference, creditValueDate, creditAccount, correspondentCountryIsoCode, bicncr, " +
            "benefCorrespondentAccount, benefCorrespondentAccount, benefCorrespondentBankName, intermediaryBankName, " +
            "mt103PlusFlag, beneficiaryAccountIban, beneficiaryName, beneficiaryAddress, beneficiaryBic, " +
            "beneficiaryBankAccount, beneficiaryBankBic, beneficiaryBankBranch, correspondentBankBic, " +
            "intermediaryBankBic, infoToBeneficiaryBank, periode, orderingPartyAdviceLanguage, " +
            "orderingPartyAdviceLngDesc , transferMode, transferModeDesc, " +
            "orderingPartyOnSwiftFlag, orderingPartyInstitution, orderingPartyBic, bdlZone, ourAccount, " +
            "bankOperationCode, instructionCode, infoToCorrespondent, charge, chargesDescription, information23, swiftCommissionTypeCode, " +
            "image, paymentEconomicCode, paymentEconomicLib, paymentCountry, paymentCountryLib, paymentResidence, " +
            "paymentResidenceLib, paymentReportingCode, paymentReportingLib, paymentAccountType, benefKeySwift, " +
            "benefCommCode, benefBankKeySwift, benefBankCommCode, benefCorrespKeySwift, benefCorrespCommCode, " +
            "intermediaryBankCommCode, ourCorrespKeySwift, ourCorrespCommCode, orderingPartyKeySwift, ibanControl, " +
            "residenceCountryIso, residenceCountryName, orderingPartyCommCode, account50k, debitAccountType, " +
            "beneficiaryCountry, totalDebitCommissionAmount, totalCreditCommissionAmount, cutoffDate, debitAccountLedger, debitAccountLedgerDesc, creditAccountLedger, " +
            "creditAccountLedgerDesc, buySellCode,registrationReference FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME +
            ".getSwiftTrsfDetails(?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(operationReference);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SwiftTransferBO> swiftTransferBOList = QueryUtils.executeNativeQuery(sql, SwiftTransferBO.class, params, em);
        return swiftTransferBOList != null && swiftTransferBOList.size() > 0 ? swiftTransferBOList.get(0) : null;
    }

    /**
     *
     * @param clientFilter
     * @param username
     * @param applic
     * @param language
     * @return List of ClientBO
     */
    public List<ClientCommonBO> getClientBOList(String clientFilter, String username, String applic,
                                                String language) {
        //        return commonGenericCommonModelSessionEJBLocal.getClientCommonBOList(clientFilter, username, applic, language);
        List<ClientCommonBO> result =
            commonGenericCommonModelSessionEJBLocal.getClientBaseBOList(clientFilter, username, applic, language, ClientCommonBO.class);
        return result;
    }

    public List<ExchangeReferenceBO> getExchangeReferenceBOList(String client, String forceReference, String applic, String username,
                                                                String language) {
        return paymentsExchangeTicketCommonModelSessionEJBBeanLocal.getExchangeReferenceList(client, forceReference, applic, username, language);
    }

    public Boolean isClientIdInFxy(String transferType, String debitClientId) {
        return FxyUtils.checkFxyParameterYExists("CHAFIC", transferType, em, debitClientId);
    }

    public List<DetailedClientBO> getDbDetailedClientBOList(String genericFilter, String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getDetailedClientBOList(genericFilter, username, applic, language);

    }

    public List<DetailedClientBO> getDebitClientList(String genericFilter, String username, String applic, String language) {
        List<DetailedClientBO> detailedClientBOList =
            commonGenericCommonModelSessionEJBLocal.getClientList(genericFilter, "H,I,B", username, applic, language);
        return detailedClientBOList;
    }

    private SepaTransferPreValidateResponse sepaTransferPreValidate(String reference, String username, String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".SepaTransferPreValidate");

        call.addNamedArgument("p_i_reference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_reference");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(reference);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SepaTransferPreValidateResponse resp = new SepaTransferPreValidateResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    private SwiftTransferPreValidateResponse swiftTransferPreValidate(String reference, String username, String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".swiftTransferPreValidate");

        call.addNamedArgument("p_i_reference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_reference");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(reference);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        SwiftTransferPreValidateResponse resp = new SwiftTransferPreValidateResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }


    private ClearingTransferPreValidateResponse clearingTransferPreValidate(String reference, String username, String applic, String language) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_GLOBAL_PACKAGE_NAME + ".clearingTransferPreValidate");

        call.addNamedArgument("p_i_reference", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_user", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_applic", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_lng", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_reference");
        query.addArgument("p_i_user");
        query.addArgument("p_i_applic");
        query.addArgument("p_i_lng");

        List queryArgs = new ArrayList();
        queryArgs.add(reference);
        queryArgs.add(username);
        queryArgs.add(applic);
        queryArgs.add(language);

        List results = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) results.get(0);

        ClearingTransferPreValidateResponse resp = new ClearingTransferPreValidateResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        return resp;
    }

    public ChaficPreInsertUpdateResponse chaficPreInsertUpdate(String transferMode, String strongCurrencyIsoCode, String weakCurrencyIsoCode,
                                                               String exchangeReference, String exchangeBuySellCode, String debitCurrencyIsoCode,
                                                               String creditCurrencyIsoCode, String applic) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_modev", JDBCTypes.VARCHAR_TYPE, transferMode));
        params.add(new PlsqlInputParameter("p_i_dev1", JDBCTypes.VARCHAR_TYPE, strongCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_dev2", JDBCTypes.VARCHAR_TYPE, weakCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_refchafic", JDBCTypes.VARCHAR_TYPE, exchangeReference));
        params.add(new PlsqlInputParameter("p_i_xchaficav", JDBCTypes.VARCHAR_TYPE, exchangeBuySellCode));
        params.add(new PlsqlInputParameter("p_i_devd", JDBCTypes.VARCHAR_TYPE, debitCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_devc", JDBCTypes.VARCHAR_TYPE, creditCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlOutputParameter("p_o_inschafic", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_updchafic", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_xchaficValid", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_CAPB_CHAFIC_PACKAGE_NAME, "trt_chafic_pre_insupd", params, em);

        ChaficPreInsertUpdateResponse resp = new ChaficPreInsertUpdateResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        resp.setInsertChaficFlag((String) returnParams.get("p_o_inschafic"));
        resp.setUpdateChaficFlag((String) returnParams.get("p_o_updchafic"));
        resp.setChaficValidFlag((String) returnParams.get("p_o_xchaficValid"));
        return resp;
    }

    public GetChaficOperationNumberResponse getChaficOperationNumber(String strongCurrencyIsoCode, String weakCurrencyIsoCode,
                                                                     String insertChaficFlag, String exchangeReference) {
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);
        Session session = jpaEntityManager.getActiveSession();

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall();
        call.setProcedureName(PLSQL_CAPB_CHAFIC_PACKAGE_NAME + ".get_chafic_oper_nooper");

        call.addNamedArgument("p_i_dev1", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_dev2", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_i_xinschafic", JDBCTypes.VARCHAR_TYPE);

        call.addNamedInOutputArgument("p_io_refchafic", JDBCTypes.VARCHAR_TYPE);

        call.addNamedOutputArgument("p_o_nooper", JDBCTypes.VARCHAR_TYPE);
        call.addNamedOutputArgument("p_o_errtxt", JDBCTypes.VARCHAR_TYPE);

        call.setResult(JDBCTypes.VARCHAR_TYPE);

        DataReadQuery query = new DataReadQuery();
        query.setCall(call);
        query.addArgument("p_i_dev1");
        query.addArgument("p_i_dev2");
        query.addArgument("p_i_xinschafic");
        query.addArgument("p_io_refchafic");

        List queryArgs = new ArrayList();

        queryArgs.add(strongCurrencyIsoCode);
        queryArgs.add(weakCurrencyIsoCode);
        queryArgs.add(insertChaficFlag);
        queryArgs.add(exchangeReference);

        List result = (List) session.executeQuery(query, queryArgs);
        DatabaseRecord record = (DatabaseRecord) result.get(0);

        GetChaficOperationNumberResponse resp = new GetChaficOperationNumberResponse();
        resp.setFnReturn((String) record.get("RESULT"));
        resp.setErrorText((String) record.get("p_o_errtxt"));
        resp.setOperationNumber((String) record.get("p_o_nooper"));
        resp.setExchangeRefernce((String) record.get("p_io_refchafic"));
        return resp;
    }

    private GetExchangeRefFromOperRefResponse getExchangeRefFromOperRef(String strongCurrencyIsoCode, String weakCurrencyIsoCode,
                                                                        String insertChaficFlag, String operationReference) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_dev1", JDBCTypes.VARCHAR_TYPE, strongCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_dev2", JDBCTypes.VARCHAR_TYPE, weakCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_xinschafic", JDBCTypes.VARCHAR_TYPE, insertChaficFlag));
        params.add(new PlsqlInputParameter("p_i_nooper", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlOutputParameter("p_o_refchafic", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_CAPB_CHAFIC_PACKAGE_NAME, "get_refchafic_from_nooper", params, em);

        GetExchangeRefFromOperRefResponse resp = new GetExchangeRefFromOperRefResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        resp.setExchangeReference((String) returnParams.get("p_o_refchafic"));
        return resp;
    }

    public ChaficPostInsertResponse chaficPostInsert(Date operationDate, String operationNumber, String exchangeReference, BigDecimal debitAmount,
                                                     BigDecimal creditAmount, Date debitValueDate, Date creditValueDate, String debitClientId,
                                                     String creditClientId, String debitClientName, String creditClientName, String debitAccount,
                                                     String creditAccount, String debitCurrencyIsoCode, String creditCurrencyIsoCode,
                                                     String fixedCurrencyIsoCode, String strongCurrencyIsoCode, String weakCurrencyIsoCode,
                                                     BigDecimal exchangeRate, String exchangeBuySellCode, String adviceDesc, String initiatorUsercode,
                                                     String validOperation, String validChaficFlag, String insertChaficFlag, String updateChaficFlag,
                                                     String version, String applic) {
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_datoper", JDBCTypes.DATE_TYPE, operationDate));
        params.add(new PlsqlInputParameter("p_i_nooper", JDBCTypes.VARCHAR_TYPE, operationNumber));
        params.add(new PlsqlInputParameter("p_i_refchafic", JDBCTypes.VARCHAR_TYPE, exchangeReference));
        params.add(new PlsqlInputParameter("p_i_mntdevd", JDBCTypes.NUMERIC_TYPE, debitAmount));
        params.add(new PlsqlInputParameter("p_i_mntdevc", JDBCTypes.NUMERIC_TYPE, creditAmount));
        params.add(new PlsqlInputParameter("p_i_datvald", JDBCTypes.DATE_TYPE, debitValueDate));
        params.add(new PlsqlInputParameter("p_i_datvalc", JDBCTypes.DATE_TYPE, creditValueDate));
        params.add(new PlsqlInputParameter("p_i_clientd", JDBCTypes.VARCHAR_TYPE, debitClientId));
        params.add(new PlsqlInputParameter("p_i_clientc", JDBCTypes.VARCHAR_TYPE, creditClientId));
        params.add(new PlsqlInputParameter("p_i_nomd", JDBCTypes.VARCHAR_TYPE, debitClientName));
        params.add(new PlsqlInputParameter("p_i_nomc", JDBCTypes.VARCHAR_TYPE, creditClientName));
        params.add(new PlsqlInputParameter("p_i_compted", JDBCTypes.VARCHAR_TYPE, debitAccount));
        params.add(new PlsqlInputParameter("p_i_comptec", JDBCTypes.VARCHAR_TYPE, creditAccount));
        params.add(new PlsqlInputParameter("p_i_devised", JDBCTypes.VARCHAR_TYPE, debitCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_devisec", JDBCTypes.VARCHAR_TYPE, creditCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_devmntcertain", JDBCTypes.VARCHAR_TYPE, fixedCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_dev1", JDBCTypes.VARCHAR_TYPE, strongCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_dev2", JDBCTypes.VARCHAR_TYPE, weakCurrencyIsoCode));
        params.add(new PlsqlInputParameter("p_i_cours12", JDBCTypes.NUMERIC_TYPE, exchangeRate));
        params.add(new PlsqlInputParameter("p_i_xchaficav", JDBCTypes.VARCHAR_TYPE, exchangeBuySellCode));
        params.add(new PlsqlInputParameter("p_i_pourcpt", JDBCTypes.VARCHAR_TYPE, adviceDesc));
        params.add(new PlsqlInputParameter("p_i_expl", JDBCTypes.VARCHAR_TYPE, initiatorUsercode));
        params.add(new PlsqlInputParameter("p_i_opervalide", JDBCTypes.VARCHAR_TYPE, validOperation));
        params.add(new PlsqlInputParameter("p_i_xchaficvalid", JDBCTypes.VARCHAR_TYPE, validChaficFlag));
        params.add(new PlsqlInputParameter("p_i_xinschafic", JDBCTypes.VARCHAR_TYPE, insertChaficFlag));
        params.add(new PlsqlInputParameter("p_i_xupdchafic", JDBCTypes.VARCHAR_TYPE, updateChaficFlag));
        params.add(new PlsqlInputParameter("p_i_version", JDBCTypes.VARCHAR_TYPE, version));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_CAPB_CHAFIC_PACKAGE_NAME, "trt_chafic_post_ins", params, em);

        ChaficPostInsertResponse resp = new ChaficPostInsertResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        return resp;
    }

    public GetPassbookFlagResponse getPassbookFlag(String operationReference, String applic, String username, String language) {
        return commonGenericCommonModelSessionEJBLocal.getPassbookFlag(operationReference, applic, username, language);
    }

    public boolean doesBankBicExist(String bankBic) {
        if (StringUtils.isEmpty(bankBic))
            return false;
        BankBic _bankBic = findBankBicByBic(bankBic);
        return _bankBic != null;
    }

    /********** Treasury Transfer **********/
    public SwiftTransferContextBO getGenerateTreasuryTransferContextBO(String operationReference, String wizardMode, String accessContext,
                                                                       String clientId, String username, String applic, String language) {
        SwiftTransferContextBO ctx = new SwiftTransferContextBO();

        setCoreBankingLanguage(language);
        BankState bankState = getBankState();
        ctx.setBankState(bankState);
        ctx.setAccessContext(accessContext);
        ctx.setTransferType(TransferTypeEnum.Swift);

        if (bankState != null)
            ctx.setCtrlMinAllowedTransferDate(bankState.getCurrentDate());

        // Language
        if (language == null)
            throw new IllegalArgumentException("Invalid User language");
        ctx.setUserLanguage(language);

        // User
        User user = getUser(username);
        if (user == null)
            throw new IllegalArgumentException(translate("MSG_INVALID_BANK_USER", language));
        ctx.setUser(user);

        LegacyUser legacyUser = getLegacyUser(user.getUsercode());
        ctx.setLegacyUser(legacyUser);

        if (!StringUtils.isEmpty(clientId))
            ctx.setDebitClientBO(new DetailedClientBO(clientId));

        ctx.setWizardMode(wizardMode == null ? PaymentsExternalTransferModelConstants.CREATE : wizardMode);

        //Get the bank user activity list
        GetBankUserActivityListResponse bankUserAccListResp = commonGenericCommonModelSessionEJBLocal.getBankUserActivityList(legacyUser.getExpl());
        ctx.getErrorResponse().handlePLSQLFunctionResponse(bankUserAccListResp);
        if (!bankUserAccListResp.hasError())
            ctx.getLegacyUser().setActivityList(bankUserAccListResp.getActivityList());

        if (!PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode))
            ctx.setOperationDate(bankState.getCurrentDate());

        // Creating The Swift Context
        if (!PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode)) {
            if (operationReference == null)
                ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF_NULL", language));
            else {
                ctx.setOperationReference(operationReference);
                //Get data of step 0
                GetOperationBenefBankBicAndCountryResponse resp = getOperationBenefBankBicAndCountry(operationReference, username, applic, language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(resp);

                if (resp.getBeneficiaryBankBic() != null) {
                    List<BankBicBO> bankBicBOList =
                        getSepaAndBankBicSearch(null, null, resp.getBeneficiaryBankBic(), null, null, null, "N", null, null);
                    if (bankBicBOList != null && bankBicBOList.size() > 0)
                        ctx.setBeneficiaryBankBic(bankBicBOList.get(0));

                    ctx.setBeneficiaryBankBicId(resp.getBeneficiaryBankBic());
                }

                if (resp.getBeneficiaryCountryIsoCode() != null) {
                    CountryBO countryBO = new CountryBO(resp.getBeneficiaryCountryIsoCode());
                    countryBO.setName(resp.getBeneficiaryCountryName());
                    ctx.setBeneficiaryBankCountryBO(countryBO);
                    ctx.setBeneficiaryBankCountryCode(resp.getBeneficiaryCountryIsoCode());
                }
            }

            SwiftTransferBO initialTrsf = getSwiftTransferBO(ctx.getOperationReference(), username, TransferTypeEnum.Swift.toString(), language);
            if (initialTrsf == null)
                ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_INITIAL_TRSF", language));
            else
                fillInitialSwiftContextBO(initialTrsf, ctx);
        }

        if (ctx.hasError())
            return ctx;

        // check if filtering on user own accounts is needed
        ctx.setFilterUserOwnAccounts(isFilterUserOwnAccounts());

        //retrieve default attributes
        GetDefaultAdviceGenerationTypeResponse defaultAdviceGenTypeResp =
            getDefaultAdviceGenerationType(TransferTypeEnum.Swift.toString(), ctx.getUser().getServiceCode());
        ctx.setDefaultAdviceTypeResponse(defaultAdviceGenTypeResp);
        ctx.getErrorResponse().handlePLSQLFunctionResponse(defaultAdviceGenTypeResp);

        String orderingPartyBicAccessibleFlag = FxyUtils.getFxyValue("TRESA", "PARGEN", "y1", em, "BICDO");
        ctx.setOrderingPartyBicAccessible(PaymentsExternalTransferModelConstants.YES.equalsIgnoreCase(orderingPartyBicAccessibleFlag));

        String correspondentBicAccessibleFlag = FxyUtils.getFxyValue("TRESA", "PARGEN", "y1", em, "BICNCR");
        ctx.setCorrespondentBicAccessible(PaymentsExternalTransferModelConstants.YES.equalsIgnoreCase(correspondentBicAccessibleFlag));

        if (PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode)) {
            BankBicBO bankBicBO = ctx.getBeneficiaryBankBic();
            if (bankBicBO != null && "Y".equals(bankBicBO.getKeySwiftFlag()))
                ctx.setKeySwift("S");
        }

        return ctx;
    }

    public SwiftTransferContextBO getGenerateTreasuryTransferContextBONext1(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            String username = ctx.getUser() != null ? ctx.getUser().getUsername() : null;
            String language = ctx.getUserLanguage();
            String wizardMode = ctx.getWizardMode();

            // Debit
            String debitAccount = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getAccountNumber() : null;
            String debitAccountCurrency =
                ctx.getDebitAccount() != null && ctx.getDebitAccount().getCurrency() != null ? ctx.getDebitAccount().getCurrency().getIsoCode() :
                null;
            String debitAccountType = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getTyp() : null;

            String transferMode = ctx.getTransferMode() != null ? ctx.getTransferMode().getCode() : null;
            String checkNumber = ctx.getChequeNumber();
            String transferCurrency = ctx.getTransferCurrency() != null ? ctx.getTransferCurrency().getIsoCode() : null;
            String exchangeReference = ctx.getExchangeReferenceBO() != null ? ctx.getExchangeReferenceBO().getReference() : null;
            BigDecimal counterValueAmount = ctx.getCounterValueAmount();

            //clear all previous error messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(language);

            //save the debit client in the client entity
            if (ctx.getDebitClientBO() != null) {
                Client client = findClientById(ctx.getDebitClientBO().getClientId());
                ctx.setClient(client);

                if (client != null) {
                    GetOrderingPartyNameAndCityResponse getOrderingPartyNameAndCityResponse =
                        getOrderingPartyNameAndCity(client.getClient(), null, null);

                    ctx.setOrderingPartyNameAndCityResponse(getOrderingPartyNameAndCityResponse);
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(getOrderingPartyNameAndCityResponse);
                }
            }

            boolean hasDebitAccountChanged = true;
            boolean hasChequeNumberChanged = true;
            if (PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode)) {
                if (ctx.getDebitAccount() != null && ctx.getInitialTransfer() != null) {
                    hasDebitAccountChanged = !Objects.equals(debitAccount, ctx.getInitialTransfer().getDebitAccount());
                    hasChequeNumberChanged = !Objects.equals(checkNumber, ctx.getInitialTransfer().getChequeNumber());
                }
            }

            //recontrol on exchange rate
            if (ctx.getAmount() != null && ctx.getTransferCurrency() != null && ctx.getDebitAccount() != null &&
                ctx.getDebitAccount().getCurrency() != null && ctx.getExchangeRate() != null) {
                CalculateAmountCounterValueResponse calcMntCountervalueResp =
                    getCalculateAmountCounterValue(transferCurrency, ctx.getAmount(), debitAccountCurrency, debitAccountCurrency,
                                                   ctx.getExchangeRate(), username, TransferTypeEnum.Swift.toString(), language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(calcMntCountervalueResp, ExternalTransferValidationEnum.ExchangeRate);

                if (ctx.hasError())
                    return ctx;

                if (!ctx.getStrongCurrencyIsoCode().equals(calcMntCountervalueResp.getStrongCurrencyIsoCode()) ||
                    !ctx.getWeakCurrencyIsoCode().equals(calcMntCountervalueResp.getWeakCurrencyIsoCode())) {

                    ctx.getErrorResponse().addWarningText(translate("MSG_CCY_RESET", language));
                    ctx.setStrongCurrencyIsoCode(calcMntCountervalueResp.getStrongCurrencyIsoCode());
                    ctx.setWeakCurrencyIsoCode(calcMntCountervalueResp.getWeakCurrencyIsoCode());
                    ctx.setExchangeRate(calcMntCountervalueResp.getRate());
                    ctx.setCounterValueAmount(calcMntCountervalueResp.getCounterValueAmount());
                }
            }

            //If in modification mode and if the transfer currency was changed, reset the correspondent info
            if (!PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode) && ctx.getInitialTransfer() != null &&
                ctx.getTransferCurrency() != null && !ctx.getInitialTransfer().getCreditCurrencyCode().equals(transferCurrency)) {
                ctx.setCorrespondantBic(null);
                ctx.setCorrespondent(null);
            }

            //control on weekend dates
            String stateCountryIsoCode = ctx.getBankState().getCountry().getIsoCode2Chars();

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode) &&
                  ctx.getOperationDate().equals(ctx.getInitialTransfer().getOperationDate()))) {

                if (ctx.getOperationDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkOperationDateWeekendResp =
                        getCheckIfDateIsOnWeekend(ctx.getOperationDate(), stateCountryIsoCode, debitAccountCurrency);

                    if (checkOperationDateWeekendResp != null && checkOperationDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.OperationDate,
                                                                      translate("PROMPT_HOLIDAY_1", language) + " " +
                                                                      translate("OPERATION_DATE", language) + " " +
                                                                      translate("QUESTION_MARK", language));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode) &&
                  Objects.equals(ctx.getFirstExecutionDate(), ctx.getInitialTransfer().getNextExecutionDate()))) {

                if (ctx.getFirstExecutionDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkFirstExecDateWeekendResp =
                        getCheckIfDateIsOnWeekend(ctx.getFirstExecutionDate(), stateCountryIsoCode, debitAccountCurrency);

                    if (checkFirstExecDateWeekendResp != null && checkFirstExecDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FirstExecutionDate,
                                                                      translate("PROMPT_HOLIDAY_1", language) + " " +
                                                                      translate("EXECUTION_DATE", language) + " " +
                                                                      translate("QUESTION_MARK", language));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode) &&
                  Objects.equals(ctx.getFinalMaturityDate(), ctx.getInitialTransfer().getFinalMaturityDate()))) {

                if (ctx.getFinalMaturityDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkFinalMaturityDateWeekendResp =
                        getCheckIfDateIsOnWeekend(ctx.getFinalMaturityDate(), stateCountryIsoCode, debitAccountCurrency);
                    if (checkFinalMaturityDateWeekendResp != null && checkFinalMaturityDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.FinalMaturityDate,
                                                                      MessageFormat.format("{0} {1} {2}", translate("PROMPT_HOLIDAY_1", language),
                                                                                           translate("MATURITY_DATE", language),
                                                                                           translate("QUESTION_MARK", language)));
                    }
                }
            }

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode) &&
                  Objects.equals(ctx.getDebitValueDate(), ctx.getInitialTransfer().getDebitValueDate()))) {

                if (ctx.getDebitValueDate() != null) {
                    CheckIfDateIsOnWeekendResponse checkDebitValueDateWeekendResp =
                        getCheckIfDateIsOnWeekend(ctx.getDebitValueDate(), stateCountryIsoCode, debitAccountCurrency);
                    if (checkDebitValueDateWeekendResp != null && checkDebitValueDateWeekendResp.getIsOnWeekend()) {
                        ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.DebitValueDate,
                                                                      translate("PROMPT_HOLIDAY_1", language) + " " +
                                                                      translate("DEBIT_VALUE_DATE", language) + " " +
                                                                      translate("QUESTION_MARK", language));
                    }
                }
            }

            //control on ordering bic
            if (ctx.getOrderingPartyBic() != null && "1".equals(StringUtils.substringSafelyToLength(ctx.getOrderingPartyBic(), 7, 1)))
                ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.OrderingPartyBic, translate("MSG_INVALID_BIC", language));

            //Control on transfer exchange reference
            if (ctx.getStrongCurrencyIsoCode() != null && ctx.getWeakCurrencyIsoCode() != null && counterValueAmount != null &&
                ctx.getDebitAccount() != null) {
                GetExchangeAuthorizedLimitResponse checkExchangeAuthorizedLimit =
                    paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAuthorizedLimit(ctx.getStrongCurrencyIsoCode(),
                                                                                                      ctx.getWeakCurrencyIsoCode(), null,
                                                                                                      exchangeReference, counterValueAmount,
                                                                                                      debitAccountCurrency,
                                                                                                      ctx.getLegacyUser().getAcces(),
                                                                                                      ctx.getBankState().getDevref(), language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkExchangeAuthorizedLimit);
            }

            if (exchangeReference != null && counterValueAmount != null) {
                GetExchangeAmountResponse getExchangeAmountResponse =
                    paymentsExchangeTicketCommonModelSessionEJBBeanLocal.checkExchangeAmount(exchangeReference, counterValueAmount);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getExchangeAmountResponse);
            }

            //now starting ui package for virest next2
            CheckIsTillModelExistentResponse checkIsTillModelExsitentResponse = checkIsTillModelExistent();

            CheckIsTillControlledForSwiftTransferModeResponse checkIsTillControlledForSwiftTransferModeResponse =
                checkIsTillControlledForSwiftTransferMode(transferMode);

            if (checkIsTillModelExsitentResponse != null && checkIsTillModelExsitentResponse.getIsTillModelExistent() &&
                checkIsTillControlledForSwiftTransferModeResponse != null &&
                checkIsTillControlledForSwiftTransferModeResponse.getIsTillControlled()) {

                //read the till variables for the user logged in : this is taken from pk_capb_ui_et_virest.step2

                String userTill = getCashierClient(ctx.getUser().getUsercode());
                String branchTill = getCashierClient(ctx.getUser().getBranchCode());

                if (!ctx.getClient().getClient().equalsIgnoreCase(userTill) && !ctx.getClient().getClient().equalsIgnoreCase(branchTill)) {
                    ctx.getErrorResponse().addErrorText(translate("PROMPT_TILL_CONTROL_1", language) + " " + userTill + " " +
                                                        translate("PROMPT_TILL_CONTROL_2", language) + " " + branchTill);
                }
            }

            if (!"I".equalsIgnoreCase(debitAccountType)) {
                GetAccount50kFlagResponse getAccount50kFlagResponse = getAccount50kFlag();
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getAccount50kFlagResponse);

                //return at this stage in case of error
                if (ctx.hasError())
                    return ctx;

                String account50kFlag = getAccount50kFlagResponse != null ? getAccount50kFlagResponse.getAccount50kFlag() : null;
                String _account50k = null;

                if ("O".equalsIgnoreCase(account50kFlag))
                    _account50k =
                        ctx.getDebitAccount().getCptdeve() == null ? debitAccount :
                        StringUtils.substringSafelyToLength(ctx.getDebitAccount().getCptdeve().trim(), 0, 11);
                else
                    _account50k = ctx.getDebitAccount() != null ? debitAccount : "";

                if (ctx.getBankState().getCountry().getIbanLength() != null && ctx.getDebitAccount() != null) { //instead of test_cee_iban
                    CalculateRibKeyResponse calculateRibKeyResponse = calculateRibKey(ctx.getBankState().getCodbnq(), ctx.getDebitAccount().getBranchCode() //TODO the second parametere is counter code, but in UI package plsql it is ui.gAgenced, to be checked later on
                                                                                      , _account50k);
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(calculateRibKeyResponse);

                    //Continue only if there is no error
                    if (!calculateRibKeyResponse.hasError()) {
                        String ribKey = calculateRibKeyResponse.getRibKey();

                        CalculateIbanFromRibKeyResponse calculateIbanFromRibKeyResponse = calculateIbanFromRibKey(ctx.getBankState().getCodbnq(), ctx.getDebitAccount().getBranchCode() //TODO same comment of this parameter as above
                                                                                                                  , _account50k, ribKey);
                        ctx.getErrorResponse().handlePLSQLFunctionResponse(calculateIbanFromRibKeyResponse);

                        if (!calculateIbanFromRibKeyResponse.hasError()) {
                            String tempAccount50k = calculateIbanFromRibKeyResponse.getIban();
                            ctx.setAccount50k(tempAccount50k);
                        }
                    } //end of no error

                } else //if not cee iban (iban not activated)
                    ctx.setAccount50k(_account50k);

                if (ctx.hasError())
                    return ctx;
            }

            if (ctx.getTransferCurrency() != null && ctx.getAmount() != null && ctx.getDebitAccount() != null) {
                CheckIsAmountInLedgerLimitResponse checkIsAmountInLedgerLimitResponse =
                    checkIsAmountInLedgerLimit(ctx.getAmount(), ctx.getDebitAccount().getAvailableBalance(), transferCurrency,
                                               ctx.getDebitAccount().getLedger().getLedger());
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkIsAmountInLedgerLimitResponse);
            }

            //check operation position code (Xposoper)
            if (ctx.getDebitAccount() != null && counterValueAmount != null) {
                //                GetOperOverdraftInfoResponse getOverdraftInfoResp =
                //                    paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, debitAccount,
                //                                                                                    counterValueAmount, username,
                //                                                                                    TransferTypeEnum.Swift.toString(),
                //                                                                                    language);
                //                ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);
                //ES 20180412 CBKSE-734 In modify mode, reference number should be sent
                GetOperOverdraftInfoResponse getOverdraftInfoResp = new GetOperOverdraftInfoResponse();
                if (!PaymentsExternalTransferModelConstants.CREATE.equals(ctx.getWizardMode())) {
                    getOverdraftInfoResp =
                        paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(ctx.getOperationReference(), debitAccount, counterValueAmount,
                                                                                        username, TransferTypeEnum.Swift.toString(), language);
                } else {
                    getOverdraftInfoResp =
                        paymentsTransferCommonModelSessionEJBLocal.getOperOverdraftInfo(null, debitAccount, counterValueAmount, username,
                                                                                        TransferTypeEnum.Swift.toString(), language);
                }
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getOverdraftInfoResp);

                if (getOverdraftInfoResp != null && !getOverdraftInfoResp.hasError()) {
                    ctx.setOperationPositionCodeTmp(getOverdraftInfoResp.getOperPositionCode());
                    if ("C".equals(getOverdraftInfoResp.getOperPositionCode()))
                        ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_OVERDRAFT", language));
                    else if ("B".equals(getOverdraftInfoResp.getOperPositionCode()))
                        ctx.getErrorResponse().addAcknowledgementText(translate("PROMPT_NOT_OVERDRAFT", language));
                }
            }

            //source: ordering p name.WHEN_VALIDATE_ITEM
            String orderingPartyCountryIso =
                ctx.getClient() != null && ctx.getClient().getTaxCountry() != null ? ctx.getClient().getTaxCountry().getIsoCode2Chars() : null;
            String orderingPartyPostalCode =
                ctx.getDefaultOrderingPartyAttributes() != null ? ctx.getDefaultOrderingPartyAttributes().getPostalCode() : null;

            if (orderingPartyCountryIso != null && orderingPartyPostalCode != null && ctx.getDebitAccount() != null) {
                CheckIs50fValidResponse checkIs50fValidResponse =
                    checkIs50fValid(orderingPartyCountryIso, ctx.getAccount50k(), debitAccount, "A.DORDRED", ctx.getOrderingPartyAddress1(),
                                    ctx.getOrderingPartyAddress2(), ctx.getOrderingPartyName1(), ctx.getOrderingPartyName2(),
                                    orderingPartyPostalCode);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(checkIs50fValidResponse);

                if (ctx.getDebitAccount() != null) {
                    checkIs50fValidResponse =
                        checkIs50fValid(orderingPartyCountryIso, ctx.getAccount50k(), debitAccount, "A.DORDRED2", ctx.getOrderingPartyAddress1(),
                                        ctx.getOrderingPartyAddress2(), ctx.getOrderingPartyName1(), ctx.getOrderingPartyName2(),
                                        orderingPartyPostalCode);
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(checkIs50fValidResponse);
                } //end of ui package for virest next2
            }

            // back to ui package external
            if (ctx.getTransferMode() != null && ctx.getSwiftPeriodicity() != null && ctx.getDebitAccount() != null) {
                GetChequeNumAndRelativeRefAccessInfoResponse getCheckNumAndRelativeRefAccessInfoResponse =
                    getChequeNumAndRelativeRefAccessInfo(transferMode, ctx.getSwiftPeriodicity().getCode(), ctx.getDebitAccount().getLornos(),
                                                         username, TransferTypeEnum.Swift.toString(), language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getCheckNumAndRelativeRefAccessInfoResponse);

                if (getCheckNumAndRelativeRefAccessInfoResponse != null && !getCheckNumAndRelativeRefAccessInfoResponse.hasError()) {
                    ctx.setChequeNumAndRefRelAccessInfo(getCheckNumAndRelativeRefAccessInfoResponse);
                    //setting the access rights

                    ctx.setAccRelativeReference(getCheckNumAndRelativeRefAccessInfoResponse.getAccRelativeRef());
                    ctx.setAccChequeNumber(getCheckNumAndRelativeRefAccessInfoResponse.getAccChequeNumber());

                    //validate cheque number and periodicity
                    if ("Y".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) &&
                        ctx.getChequeAttributes() != null && ctx.getChequeAttributes().getChequeTransferModeList() != null &&
                        ctx.getChequeAttributes().getChequeTransferModeList().contains(transferMode) &&
                        !"U".equals(ctx.getSwiftPeriodicity().getCode()) && checkNumber != null) {

                        ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.ChequeNumber,
                                                            translate("MSG_INVALID_NUMCHQ_PERIOD", language));
                    }

                    if (ctx.hasError())
                        return ctx;

                    if ("O".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberControlled()) &&
                        "Y".equalsIgnoreCase(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) &&
                        ctx.getChequeAttributes() != null && ctx.getChequeAttributes().getChequeTransferModeList() != null &&
                        ctx.getChequeAttributes().getChequeTransferModeList().contains(transferMode)) {
                        if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode) && !hasDebitAccountChanged &&
                              !hasChequeNumberChanged) && checkNumber != null) {
                            ControlChequeNumberResponse controlChequeNumberResponse =
                                controlChequeNumber(debitAccount, checkNumber, TransferTypeEnum.Swift.toString());
                            ctx.getErrorResponse().handlePLSQLFunctionResponse(controlChequeNumberResponse,
                                                                               ExternalTransferValidationEnum.ChequeNumber);
                        }
                    }

                    //Control on ordering party reference (REFDEB)
                    if (("N".equals(getCheckNumAndRelativeRefAccessInfoResponse.getIsChequeNumberEnabled()) ||
                         (ctx.getChequeAttributes() != null && ctx.getChequeAttributes().getChequeTransferModeList() != null &&
                          !ctx.getChequeAttributes().getChequeTransferModeList().contains(transferMode))) && "Q".equals(transferMode) &&
                        !"NEW".equals(ctx.getOrderingPartyReference())) {

                        ControlChequeRefForModeQResponse resp =
                            controlChequeRefForModeQ(debitAccount, ctx.getOrderingPartyReference(), ctx.getOperationReference(),
                                                     ctx.getTransferType().toString());
                        ctx.getErrorResponse().handlePLSQLFunctionResponse(resp, ExternalTransferValidationEnum.OrderingPartyReference);
                    }
                }
            }

            //set default values for the next 3 step
            if (ctx.getBankOperationCode() == null && PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode))
                ctx.setBankOperationCode(findSwiftAbbreviationByFieldAndAbbr("23", "B/CRED"));
        } //end of if swiftContextBO != null
        return ctx;
    }

    public SwiftTransferContextBO getGenerateTreasuryTransferContextBONext2(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            String username = ctx.getUser() != null ? ctx.getUser().getUsername() : null;
            String language = ctx.getUserLanguage();

            Date creditValueDate = ctx.getCreditValueDate();

            String correspondantAccount = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccount() : null;
            String debitAccount = ctx.getDebitAccount() != null ? ctx.getDebitAccount().getAccountNumber() : null;
            String transferCurrency = ctx.getTransferCurrency() != null ? ctx.getTransferCurrency().getIsoCode() : null;

            String beneficiaryName = ctx.getBeneficiaryName();
            String beneficiaryBankBicCode = ctx.getBeneficiaryBankBic() != null ? ctx.getBeneficiaryBankBic().getBicCode() : null;

            //rest all messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(language);

            //Check if debit and credit account are the same
            if (debitAccount != null && debitAccount.equalsIgnoreCase(correspondantAccount))
                ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CorrespondentAccount,
                                                              translate("MSG_SAME_DEBIT_CREDIT_ACCOUNT", language));

            String correspondentCountryIso = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getCountry() : null;
            String correspondentCurrencyIso = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getCurrency() : "";
            String correspondentAccountType = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccountType() : null;

            if (!(PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()) && ctx.getInitialTransfer() != null &&
                  creditValueDate != null && Objects.equals(ctx.getInitialTransfer().getCreditValueDate(), creditValueDate))) {
                CheckIfDateIsOnWeekendResponse checkCreditValueDateWeekendResp =
                    getCheckIfDateIsOnWeekend(creditValueDate, correspondentCountryIso, correspondentCurrencyIso);
                if (checkCreditValueDateWeekendResp != null && checkCreditValueDateWeekendResp.getIsOnWeekend()) {
                    ctx.getErrorResponse().addAcknowledgementText(ExternalTransferValidationEnum.CreditValueDate,
                                                                  translate("PROMPT_HOLIDAY_1", language) + " " +
                                                                  translate("CREDIT_VALUE_DATE", language) + " " +
                                                                  translate("QUESTION_MARK", language));
                }
            }

            String creditResidentCode = ctx.getCorrespondent() != null ? ctx.getCorrespondent().getAccountClientResidence() : null;
            if (ctx.getBeneficiaryBankBic() == null) {
                if (ctx.getBeneficiaryBic() == null)
                    creditResidentCode = ctx.getBeneficiaryBankCountryBO() != null ? ctx.getBeneficiaryBankCountryBO().getResidenceTypeCode() : null;
                else if (ctx.getBeneficiaryBic() != null && ctx.getBeneficiaryBic().getCountry() != null &&
                         ctx.getBeneficiaryBic().getCountry().getResidenceType() != null)
                    creditResidentCode = ctx.getBeneficiaryBic().getCountry().getResidenceType().getCode();
            } else {
                BankBic beneficiryBankBic = findBankBicByBic(beneficiaryBankBicCode);
                if (beneficiryBankBic != null)
                    creditResidentCode =
                        beneficiryBankBic.getCountry() != null && beneficiryBankBic.getCountry().getResidenceType() != null ?
                        beneficiryBankBic.getCountry().getResidenceType().getCode() : null;
            }

            if (ctx.getTransferCurrency() != null) {
                GetPaymentReportingTypeResponse getPaymentReportingTypeResponse =
                    paymentReportingSessionEJB.getPaymentReportingActive(ctx.getCounterValueAmount(), transferCurrency, username,
                                                                         TransferTypeEnum.Swift.toString(), language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getPaymentReportingTypeResponse);

                if (!getPaymentReportingTypeResponse.hasError()) {
                    ctx.setIsReportPaymentActive("O".equalsIgnoreCase(getPaymentReportingTypeResponse.getPaymentReportingActiveFlag()));
                    ctx.setReportPaymentTypeCode(getPaymentReportingTypeResponse.getPaymentReportingTypeCode());

                    if (getPaymentReportingTypeResponse.getPaymentReportingTypeCode() == null)
                        ctx.setIsReportPaymentActive(false);
                }
            }

            //start of the CRP function
            //Setting te parameters
            //This is not a mandatory parameter
            String beneficiaryBicCode = ctx.getBeneficiaryBic() != null ? ctx.getBeneficiaryBic().getBic() : null;
            String paymentReportActiveFlag = ctx.getIsReportPaymentActive() != null && ctx.getIsReportPaymentActive().booleanValue() ? "O" : "N";

            String infoToCorrespondent = ctx.getInfoToCorrespondent() != null ? ctx.getInfoToCorrespondent().getAbbreviation() : null;
            if (ctx.getInfoToCorrespondentText() != null)
                infoToCorrespondent += ctx.getInfoToCorrespondentText();

            if (ctx.getTransferMode() != null && ctx.getDebitAccount() != null && ctx.getCorrespondent() != null &&
                ctx.getTransferCurrency() != null && ctx.getBeneficiaryBankCountryBO() != null && ctx.getOrderingPartyNameAndCityResponse() != null) {
                GetPaymentReportDefaultValuesResponse reportPaymentDefaultValuesResponse =
                    getPaymentReportDefaultValuesResponse(ctx.getTransferMode().getCode(), paymentReportActiveFlag // "  crpActiveFlag "
                                                          , debitAccount, correspondantAccount //String  creditAccountNumber
                                                          , transferCurrency, ctx.getAmount(), beneficiaryBicCode,
                                                          ctx.getBeneficiaryBankCountryBO().getName(), // TODO ML?  beneficiaryCityName
                                                          beneficiaryBankBicCode // beneficiaryBankBicCode
                                                          , ctx.getDebitAccount().getLornos() //  p_i_DebitLornos
                                                          , ctx.getDebitAccount().getClient().getClient() //  String  debitClientId
                                                          , ctx.getCorrespondent().getAccountLornos() //   String  creditLornosCode
                                                          , ctx.getCorrespondent().getAccountClient() //  creditClientId
                                                          , ctx.getCorrespondantBic() // String  correspondentBicCode  TODO fix TYPO correspondant
                                                          , ctx.getCorrespondent().getAccountClientCountryName() //  correspondentCityName
                                                          , ctx.getReportPaymentTypeCode() //"String  reportPaymentTypeCode"
                                                          , ctx.getBeneficiaryIban() //  String  beneficiaryAccountNumber
                                                          , ctx.getBeneficiaryBankAccount() //   String  beneficiaryBankAccountNumber
                                                          , beneficiaryName, null //"String  donorBicCode"  //This is null for external transfer. Later check it for other transfers
                                                          , ctx.getOrderingPartyNameAndCityResponse().getCityName() //"String  donorCityName"   //call a function findADR
                                                          , infoToCorrespondent, ctx.getDebitAccount().getTyp() // String  debitorAccountTypeCode
                                                          , null // this value is null following the UI package
                                                          , ctx.getClient().getResid() //String  debitorResidenceCode
                                                          , correspondentAccountType //TODO correct possible old bug in UI pakcage (look at rech_typcpt that is in the next3 create virest: called after this function)
                                                          , ctx.getClient().getSiren() // String  sirenCode
                                                          , username, TransferTypeEnum.Swift.toString() //String applic
                                                          , language);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(reportPaymentDefaultValuesResponse,
                                                                   ExternalTransferValidationEnum.CreditValueDate);

                if (!reportPaymentDefaultValuesResponse.hasError()) {
                    //setting the initial values for CRP
                    ctx.setPaymentReportCode(reportPaymentDefaultValuesResponse.getPaymentReportCode());
                    ctx.setPaymentReportCrpCode(reportPaymentDefaultValuesResponse.getPaymentReportCrpCode());
                    ctx.setPaymentReportCountry(reportPaymentDefaultValuesResponse.getCountry());
                    ctx.setPaymentReportSirenCode(reportPaymentDefaultValuesResponse.getSirenCode());
                    ctx.setPaymentReportBank(reportPaymentDefaultValuesResponse.getBankCode());
                    ctx.setPaymentReportBankCounter(reportPaymentDefaultValuesResponse.getBankCounter());
                    ctx.setPaymentReportResidenceType(reportPaymentDefaultValuesResponse.getCreditResidenceCode());
                }
            }

            //end of the CRP function
            //now start UI package virest nexst 3
            BankBic beneficiaryBankBic = findBankBicByBic(beneficiaryBankBicCode);
            if (beneficiaryBankBic.getCountry() != null &&
                blockSwiftForCountryIso(correspondentCountryIso, beneficiaryBankBic.getCountry().getIsoCode2Chars()))
                ctx.getErrorResponse().addErrorText(translate("MSG_ISO_CONTROL_PARAMETER", language));

            String corresponentBicPublishedFlag = null;
            if (ctx.getCorrespondent() != null && ctx.getCorrespondent().getBic() != null) {
                if (StringUtils.substringSafelyToLength(ctx.getCorrespondent().getBic(), 7, 1).equalsIgnoreCase("1"))
                    ctx.getErrorResponse().addErrorText(translate("MSG_INVALID_CORRESPONDENT_BIC", language));
                corresponentBicPublishedFlag = ctx.getCorrespondent().getBicPublishedFlag();
            }
            corresponentBicPublishedFlag = corresponentBicPublishedFlag == null ? "N" : corresponentBicPublishedFlag;

            if ("O".equalsIgnoreCase(corresponentBicPublishedFlag) &&
                (ctx.getTransferCurrency() == null || (!"EUR".equalsIgnoreCase(transferCurrency) && !"GBP".equalsIgnoreCase(transferCurrency))))
                ctx.getErrorResponse().addErrorText(ExternalTransferValidationEnum.TransferCurrency, translate("MSG_TRSF_CUR_EUR_GBP", language));

            if (!StringUtils.isEmpty(beneficiaryName))
                correspondentAccountType =
                    beneficiaryName.toLowerCase().contains("ban") || beneficiaryName.toLowerCase().contains("bank") ||
                    beneficiaryName.toLowerCase().contains("banq") ? "B" : "C";
            ctx.setCorrespondentAccountTypeCode(correspondentAccountType);

            //calculate values for correspondent client name and address
            //TODO function name is to be reviewed
            if (ctx.getCorrespondent() != null && ctx.getCorrespondent().getAccount() != null && ctx.getCorrespondent().getAccountClient() != null) {
                GetOrderingPartyNameAndCityResponse getCorrespondentNameAndCityResponse =
                    getOrderingPartyNameAndCity(ctx.getCorrespondent().getAccountClient(), null, null);
                ctx.setCorrespondentNameAndCityResponse(getCorrespondentNameAndCityResponse);
                ctx.getErrorResponse().handlePLSQLFunctionResponse(getCorrespondentNameAndCityResponse);
            }
        }
        return ctx;
    }

    public SwiftTransferContextBO getGenerateTreasuryTransferContextBONext3(SwiftTransferContextBO ctx) {
        if (ctx != null) {
            String language = ctx.getUserLanguage();
            Date currentDate = ctx.getBankState() != null ? ctx.getBankState().getCurrentDate() : null;

            String wizardMode = ctx.getWizardMode();
            String transferType = TransferTypeEnum.Swift.toString();
            //clear all previous error messages
            if (ctx.getErrorResponse() != null)
                ctx.getErrorResponse().clearAllResponses();
            setCoreBankingLanguage(language);

            //validate that the iban is valid (in case we are in iban)
            if ("O".equalsIgnoreCase(ctx.getIbanControl())) {
                //Always convert iban to upper case
                if (ctx.getBeneficiaryIban() != null)
                    ctx.setBeneficiaryIban(ctx.getBeneficiaryIban().toUpperCase());

                if (ctx.getBeneficiaryBic() != null && ctx.getBeneficiaryBic().getCountry() != null && ctx.getBeneficiaryIban() != null) {
                    CheckIbanValidResponse ibanValidResponse =
                        getCheckIbanValid(ctx.getBeneficiaryBic().getCountry().getIsoCode2Chars(), ctx.getBeneficiaryIban(),
                                          ctx.getUser().getUsername(), transferType, language);
                    ctx.getErrorResponse().handlePLSQLFunctionResponse(ibanValidResponse, ExternalTransferValidationEnum.Iban);
                }
            }

            // Calculate maximum allowed transfer date
            Date maxAllowedTransferDate = getMaxAllowedTransferDate(ctx.getBankState().getDevref(), currentDate, transferType);
            ctx.setCtrlMaxAllowedTransferDate(maxAllowedTransferDate);
            ctx.setCtrlMinAllowedTransferDate(currentDate);

            // Get the default transfer mode
            if (ctx.getTransferMode() == null && PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode)) {
                String dftTransferModeCode = getDefaultTransferModeCode(transferType, ctx.getUser().getServiceCode());
                if (dftTransferModeCode != null)
                    ctx.setTransferMode(getSwiftTransferMode(null, dftTransferModeCode));
            }

            // Cheque number controls
            if (ctx.getChequeAttributes() == null)
                ctx.setChequeAttributes(getChequeAttributes(transferType));

            //setting intial values for step 2
            if (!PaymentsExternalTransferModelConstants.MODIFY.equals(wizardMode))
                if (ctx.getClientRelativeReference() == null)
                    ctx.setClientRelativeReference(ctx.DEFAULT_CLIENT_REF_VALUE);

            if (PaymentsExternalTransferModelConstants.CREATE.equals(wizardMode)) {
                if (ctx.getSwiftPeriodicity() == null)
                    ctx.setSwiftPeriodicity(getSwiftTransferPeriodicity("U"));

                //Set default value for ordering party on SWIFT
                if (ctx.getOrderingPartyOnSwift() == null)
                    ctx.setOrderingPartyOnSwift("N");
            }

            validateSwiftTarget(ctx);
        }
        return ctx;
    }

    public BankState getBankState() {
        TypedQuery<BankState> query = em.createNamedQuery("BankState.findAll", BankState.class);

        List<BankState> result = query.getResultList();
        return result != null && result.size() > 0 ? result.get(0) : null;
    }

    public String getStateCountryCode() {
        BankState bankState = getBankState();

        return bankState != null ? bankState.getCountry().getIsoCode2Chars() : null;
    }

    public List<CurrencyBO> getCurrencyBOList(String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getCurrencyBOList(username, applic, language);
    }

    public List<BicBO> getOrderingPartyBicList(String client, String username, String applic, String language) {
        List<BicBO> orderingPartyBicList = getBicList(client, username, applic, language);
        return orderingPartyBicList;
    }

    public List<BicBO> getCorrespondentBicList(String client, String username, String applic, String language) {
        List<BicBO> correspondentBicList = getBicList(client, username, applic, language);
        return correspondentBicList;
    }

    public List<AccountCorrespFXYFilteredBicBO> getAccountCorrespFXYFilteredBicBOList(String username, String applic, String language) {
        String sql = "SELECT code FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getAccCorrespFxyFltrList(?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(applic);
        params.add(language);


        return QueryUtils.executeNativeQuery(sql, AccountCorrespFXYFilteredBicBO.class, params, em);
    }

    public List<RTGSTransferTypeBO> getRTGSTransferTypeBOList(String username, String applic, String language) {

        List<RTGSTransferTypeBO> selectedList = new ArrayList<RTGSTransferTypeBO>();
        List<RTGSTransferTypeBO> rtgsTransferTypeBOList = getRTGSTransferModeBOList(username, applic, language);
        if (rtgsTransferTypeBOList != null && rtgsTransferTypeBOList.size() > 0) {
            List<SwiftTransferMode> swiftTransferModeList = getSwiftTransferModeSearch("N", username);
            if (swiftTransferModeList != null && swiftTransferModeList.size() > 0) {
                List<String> transferModeList = new ArrayList<String>();
                for (SwiftTransferMode swiftTransferMode : swiftTransferModeList) {
                    transferModeList.add(swiftTransferMode.getCode());
                }

                for (RTGSTransferTypeBO rtgsTransferTypeBO : rtgsTransferTypeBOList) {
                    if (transferModeList.contains(rtgsTransferTypeBO.getTransferMode())) {
                        selectedList.add(rtgsTransferTypeBO);
                    }
                }
            }
        }
        return selectedList;
    }

    public ReceivedSwiftTransferBO getReceivedSwiftTransferBO(String operationReference, String creditType, String returnToSender, String username,
                                                              String transferType, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getReceivedSwiftTransferBO(operationReference, creditType, transferType, returnToSender,
                                                                                     username, transferType, language);
    }

    public InternetTransferBO getInternetTransferBO(String operationReference, String username, String applic, String language) {
        return paymentsTransferCommonModelSessionEJBLocal.getInternetTransferBO(operationReference, username, applic, language);
    }

    public List<CountryBO> getResidenceCountryBOList(String genericFilter, String isTransferFlag, String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getCountryBOList(genericFilter, isTransferFlag, username, applic, language);
    }

    public List<GlobalCommissionBO> getGlobalCommissionList(String accountTypeD, String periodCode, String controlXCO, String perAuthorization,
                                                            String ledgerCodeD, String perception, String model, String ledgerGroupD,
                                                            String currencyCode1, String currencyCode2, String code, String commissionObligatoryFlag,
                                                            String creditAccount, String debitAccount, String debitCurrencyCode,
                                                            String transferCurrency, BigDecimal netAmountCredit, BigDecimal convertedAmount,
                                                            BigDecimal netAmountDebit, BigDecimal amount, String creditClientCode,
                                                            String debitClientCode, String debitBranchCode, String userBranchCode, String serviceCode,
                                                            String referenceNumber, Date operationDate, Date startDate, Date endDate,
                                                            Integer occurenceNumber, String x4, String x5, String commissionCode,
                                                            String commissionFlag, String operationCode, String user, String application,
                                                            String language) {
        return commissionSessionEJB.getGlobalCommissionList(accountTypeD, periodCode, controlXCO, perAuthorization, ledgerCodeD, perception, model,
                                                            ledgerGroupD, currencyCode1, currencyCode2, code, commissionObligatoryFlag, creditAccount,
                                                            debitAccount, debitCurrencyCode, transferCurrency, netAmountCredit, convertedAmount,
                                                            netAmountDebit, amount, creditClientCode, debitClientCode, debitBranchCode,
                                                            userBranchCode, serviceCode, referenceNumber, operationDate, startDate, endDate,
                                                            occurenceNumber, x4, x5, commissionCode, commissionFlag, operationCode, user, application,
                                                            language);
    }

    public boolean getSwiftArabicTransferModeFlag(String mode) {
        return FxyUtils.checkFxyParameterExists("VIREST", "CARARABE", em, mode);
    }

    public AccountDetailsResponse getAccountDetailsResponse(String accountNumber, String username, String applic, String language) {
        return commonGenericCommonModelSessionEJBLocal.getAccountDetailsResponse(accountNumber, username, applic, language);
    }

    public boolean getModifySwiftCutoffParamActiveFlag() {
        return "O".equals(FxyUtils.getFxyValue("VIREST", "PARGEN", em, "CALCDATVAL"));
    }

    public boolean getModifySepaCutoffParamActiveFlag() {
        return "O".equals(FxyUtils.getFxyValue("VIRSEP", "PARGEN", em, "CALCDATVAL"));
    }

    public List<SwiftTransferMode> getTreasuryTransferModeList(String username, String applic, String language) {
        String sql =
            "SELECT code, description, xtarget,debitLedgerGroup FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getTreasuryTransferModeList(?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SwiftTransferMode> transferModeList = QueryUtils.executeNativeQuery(sql, SwiftTransferMode.class, params, em);
        return transferModeList;
    }

    /** Private Methods **/
    private List<GlobalCommissionBO> getOperationCommissionList(String operationReference, BigDecimal debitAmount, BigDecimal creditAmount,
                                                                String debitAccount, String creditAccount, String username, String applic,
                                                                String language, String wizardMode) {
        List<GlobalCommissionBO> commissions =
            commissionSessionEJB.getOperationCommissionList(operationReference, debitAmount, creditAmount, debitAccount, creditAccount, username,
                                                            applic, language);

        if (PaymentsExternalTransferModelConstants.DUPLICATE.equals(wizardMode))
            for (GlobalCommissionBO commissionItem : commissions)
                commissionItem.setUserDefined(true);

        return commissions;
    }

    private void validateSwiftTarget(SwiftTransferContextBO ctx) {
        if (ctx != null && ctx.isSwiftTarget()) {
            boolean isBeneficiaryBicDirect = ctx.getBeneficiaryBic() != null && ctx.getBeneficiaryBic().isDirectParticipantTarget();
            boolean isBeneficiaryBankBicDirect = ctx.getBeneficiaryBankBic() != null && isBicDirect(ctx.getBeneficiaryBankBic().getBicCode());
            boolean isBeneficiaryCorrespondentBankBicDirect =
                ctx.getBeneficiaryCorrespondentBic() != null && ctx.getBeneficiaryCorrespondentBic().isDirectParticipantTarget();
            boolean isIntermediaryBankBicDirect = ctx.getIntermediaryBic() != null && ctx.getIntermediaryBic().isDirectParticipantTarget();

            if (!isBeneficiaryBicDirect && !isBeneficiaryBankBicDirect && !isBeneficiaryCorrespondentBankBicDirect && !isIntermediaryBankBicDirect)
                ctx.getErrorResponse().addErrorText(translate("AT_LEAST_ONE_OF_THE_BENEFICIARYS_BANKS_SHOULD_BE_DIRECT_PARTICIPANT_TARGET",
                                                              ctx.getUserLanguage()));

            if (PaymentsExternalTransferModelConstants.containsMoreThanOneTrue(isBeneficiaryBicDirect, isBeneficiaryBankBicDirect,
                                                                               isBeneficiaryCorrespondentBankBicDirect, isIntermediaryBankBicDirect))
                ctx.getErrorResponse().addErrorText(translate("ONLY_ONE_BANK_SHOULD_BE_DIRECT_PARTICIPANT_TARGET_PLEASE_DO_THE_NECESSARY_CORRECTIONS_IN_THE_BENEFICIARY_PART",
                                                              ctx.getUserLanguage()));
        }
    }

    private boolean isBicDirect(String bic) {
        String sql = "SELECT ptgt FROM bankfil WHERE bic = ? AND ROWNUM = 1";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, bic);

        List resultList = query.getResultList();
        String participantTarget = resultList != null && !resultList.isEmpty() ? (String) resultList.get(0) : null;
        return "D".equalsIgnoreCase(participantTarget);
    }

    private BankBic findBankBicByBic(String bic) {
        if (bic != null)
            return em.find(BankBic.class, bic);
        return null;
    }

    private List<BicBO> getBicList(String client, String username, String applic, String language) {
        String sql = "SELECT bic CODE FROM TABLE(" + PLSQL_GLOBAL_GENERIC_PACKAGE_NAME + ".get_corchq_bic_liste(?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(client);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<BicBO> bicBOList = QueryUtils.executeNativeQuery(sql, BicBO.class, params, em);
        return bicBOList;
    }

    private Account findAccountByCode(String code) {
        return em.find(Account.class, code);
    }

    private SwiftTransfer findSwiftTransferByCode(String code) {
        return em.find(SwiftTransfer.class, code);
    }

    private SwiftBDLZone findSwiftBDLZoneByCode(String swiftBDLZoneCode) {
        List<SwiftBDLZone> swiftBDLZoneList = getSwiftBDLZoneFindAll();
        if (swiftBDLZoneList != null && !swiftBDLZoneList.isEmpty())
            for (SwiftBDLZone swiftBDLZone : swiftBDLZoneList)
                if (swiftBDLZone != null && swiftBDLZone.getCode() != null && swiftBDLZone.getCode().equals(swiftBDLZoneCode))
                    return swiftBDLZone;
        return null;
    }

    private Country findCountryByCode(String countryCode) {
        return em.find(Country.class, countryCode);
    }

    private ResidenceType findResidenceTypeByCode(String residenceTypeCode) {
        return em.find(ResidenceType.class, residenceTypeCode);
    }

    private Client findClientById(String clientId) {
        return em.find(Client.class, clientId);
    }

    private SwiftCharge getSwiftCharge(String swiftChargeCode) {
        List<SwiftCharge> swiftChargeList = getSwiftChargeValue();
        if (swiftChargeList != null && !swiftChargeList.isEmpty())
            for (SwiftCharge swiftCharge : swiftChargeList)
                if (swiftCharge != null && swiftCharge.getCode() != null && swiftCharge.getCode().equals(swiftChargeCode))
                    return swiftCharge;
        return null;
    }

    private SwiftTransferPeriodicity getSwiftTransferPeriodicity(String swiftTransferPeriodicityCode) {
        List<SwiftTransferPeriodicity> swiftTransferPeriodicityList = getSwiftTransferPeriodicityFindAll();
        if (swiftTransferPeriodicityList != null && !swiftTransferPeriodicityList.isEmpty())
            for (SwiftTransferPeriodicity swiftTransferPeriodicity : swiftTransferPeriodicityList)
                if (swiftTransferPeriodicity != null && swiftTransferPeriodicity.getCode() != null &&
                    swiftTransferPeriodicity.getCode().equals(swiftTransferPeriodicityCode))
                    return swiftTransferPeriodicity;
        return null;
    }

    private TransferType getTransferType(String transferTypeCode) {
        return em.find(TransferType.class, transferTypeCode);
    }

    private SwiftTransferMode getSwiftTransferMode(String treasuryTransferFlag, String transferModeCode) {
        List<SwiftTransferMode> swiftTransferModeList = getSwiftTransferModeSearch(treasuryTransferFlag, null);

        if (swiftTransferModeList != null && !swiftTransferModeList.isEmpty())
            for (SwiftTransferMode swiftTransferMode : swiftTransferModeList)
                if (swiftTransferMode != null && swiftTransferMode.getCode() != null && swiftTransferMode.getCode().equals(transferModeCode))
                    return swiftTransferMode;

        return null;
    }

    private String getDefaultTransferModeCode(String transferType, String userServiceCode) {
        String sql = "SELECT y1 FROM fx5y8 WHERE tname='" + transferType + "' AND model='MODEVDFT' AND x1 IN ('*',?) AND substr(x1, 1, 1) <> ' ' ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, userServiceCode);

        List<String> resultList = query.getResultList();
        return resultList != null && resultList.size() > 0 ? resultList.get(0) : null;
    }

    private LegacyUser getLegacyUser(String userCode) {
        return em.find(LegacyUser.class, userCode);
    }

    private User getUser(String username) {
        return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
    }

    private String getCashierClient(String code) {
        return getFxyValue("GUICHET", "CAISSE", code);
    }

    private boolean isFilterUserOwnAccounts() {
        String sFilterUserOwnAccounts = getFxyValue(TransferTypeEnum.Swift.toString(), "PARGEN", "INTEXPL");
        return "O".equalsIgnoreCase(sFilterUserOwnAccounts);
    }

    private String getDefaultCharges() {
        return FxyUtils.getFxyValue(TransferTypeEnum.Swift.toString(), "CHARG", "X1", em, null, "O");
    }

    private static String translate(String key, String lng) {
        return PaymentsExternalTransferModelConstants.translate(key, lng);
    }

    private boolean isRTGSFlagEnabled() {
        String rtgsFlag = FxyUtils.getFxyValue("VIREST", "PARGEN", em, "RTGSIMEIB");
        return !StringUtils.isEmpty(rtgsFlag) && "O".equals(rtgsFlag);
    }

    private String getSwiftTransferType(String operationReference) {
        List<PlsqlParameter> params = new ArrayList<>();
        params.add(new PlsqlInputParameter("p_i_nooper", JDBCTypes.VARCHAR_TYPE, operationReference));

        Map<String, Object> transferModeMap = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "getVirestModev", params, em);
        return transferModeMap != null ? (String) transferModeMap.get("RESULT") : null;
    }

    /**
     * Private helper function return list of swfit transfers.
     * @param clientId
     * @param operationReference
     * @param startDate
     * @param endDate
     * @param periodCode
     * @param validityCode
     * @param beneficiaryName
     * @param beneficiaryBankCountryIso
     * @param beneficiaryBankBic
     * @return
     */

    private List<ExternalTransferBO> getSwiftTransferList(String clientId, String operationReference, Date startDate, Date endDate, String periodCode,
                                                          String validityCode, String beneficiaryName, String beneficiaryBankCountryIso,
                                                          String beneficiaryBankBic) {
        return getSwiftTransferList(clientId, operationReference, startDate, endDate, periodCode, validityCode, beneficiaryName,
                                    beneficiaryBankCountryIso, beneficiaryBankBic, null, null);
    }

    private List<ExternalTransferBO> getSwiftTransferList(String clientId, String operationReference, Date startDate, Date endDate, String periodCode,
                                                          String validityCode, String beneficiaryName, String beneficiaryBankCountryIso,
                                                          String beneficiaryBankBic, String modeTransferCode,
                                                          String serviceCode) {
        //Swift Transfer
        String jpql =
            "Select oexternalTransferBOList  From SwiftTransfer o  Where o.debitAccount.client.client = :clientId " +
            "       And o.operationReference like CONCAT('%',:reference,'%') " +
            "       And o.periodicity.code = COALESCE (:periodCode, o.periodicity.code)" +
            "       And o.validity.code  = COALESCE( :validityCode, o.validity.code)" +
            "       And o.transferMode.code = COALESCE( :modeTransferCode, o.transferMode.code)" +
            "       And o.userServiceCode.code = COALESCE( :serviceCode, o.userServiceCode.code)";

        if (startDate != null && endDate != null)
            jpql += " And o.operationDate BETWEEN :startDate AND :endDate";

        if (!StringUtils.isEmpty(beneficiaryName))
            jpql += " And LOWER(o.beneficiaryName) like CONCAT( '%', LOWER(:beneficiaryName) , '%')";

        if (!StringUtils.isEmpty(beneficiaryBankCountryIso))
            jpql += " And o.beneficiaryBankBic.country.isoCode2Chars = :beneficiaryBankCountryIso ";

        if (!StringUtils.isEmpty(beneficiaryBankBic))
            jpql += " And o.beneficiaryBankBic.bic like CONCAT ( '%', :beneficiaryBankBic, '%' )";

        Query query = em.createQuery(jpql, SwiftTransfer.class);

        query.setParameter("clientId", clientId);
        query.setParameter("reference", operationReference);
        query.setParameter("periodCode", periodCode);
        query.setParameter("validityCode", validityCode);
        query.setParameter("modeTransferCode", modeTransferCode);
        query.setParameter("serviceCode", serviceCode);

        if (startDate != null && endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
        }

        if (!StringUtils.isEmpty(beneficiaryName))
            query.setParameter("beneficiaryName", beneficiaryName);

        if (!StringUtils.isEmpty(beneficiaryBankCountryIso))
            query.setParameter("beneficiaryBankCountryIso", beneficiaryBankCountryIso);

        if (!StringUtils.isEmpty(beneficiaryBankBic))
            query.setParameter("beneficiaryBankBic", beneficiaryBankBic);

        List<SwiftTransfer> swiftTransferList = query.getResultList();
        TransferType transferType = getTransferType(TransferTypeEnum.Swift.toString());

        List<ExternalTransferBO> externalTransferBOList = new ArrayList<ExternalTransferBO>();
        for (SwiftTransfer swiftTransfer : swiftTransferList)
            externalTransferBOList.add(new ExternalTransferBO(swiftTransfer, transferType));

        return externalTransferBOList;
    }

    /**
     * Private helper function return list of clearing transfers.
     * @param clientId
     * @param operationReference
     * @param startDate
     * @param endDate
     * @param periodCode
     * @param validityCode
     * @param beneficiaryName
     * @param beneficiaryBankCountryIso
     * @return
     */
    private List<ExternalTransferBO> getClearingTransferList(String clientId, String operationReference, Date startDate, Date endDate,
                                                             String periodCode, String validityCode, String beneficiaryName,
                                                             String beneficiaryBankCountryIso) {


        return new ArrayList<>(); //TODO remove the entire fuinction//Clearing Transfer
        /*
        String jpql =
            "Select o" + "   From ClearingTransfer o" + "   Where o.debitAccount.client.client = :clientId " +
            "       And o.operationReference like CONCAT('%',:reference,'%') " +
            "       And o.periodicity.code = COALESCE (:periodCode, o.periodicity.code)" +
            "       And o.validity.code  = COALESCE( :validityCode, o.validity.code)";

        if (startDate != null && endDate != null)
            jpql += " And o.operationDate BETWEEN :startDate AND :endDate";


        if (beneficiaryName != null && !beneficiaryName.isEmpty())
            jpql += " And LOWER(o.beneficiaryName) like CONCAT( '%', LOWER(:beneficiaryName) , '%')";

        if (beneficiaryBankCountryIso != null && !beneficiaryBankCountryIso.isEmpty())
            jpql += " And o.beneficiaryBankCountry.isoCode2Chars = :beneficiaryBankCountryIso ";

        Query query = em.createQuery(jpql, SwiftTransfer.class);
        query.setParameter("clientId", clientId);
        query.setParameter("reference", operationReference);
        query.setParameter("periodCode", periodCode);
        query.setParameter("validityCode", validityCode);

        if (startDate != null && endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
        }

        if (beneficiaryName != null && !beneficiaryName.isEmpty())
            query.setParameter("beneficiaryName", beneficiaryName);

        if (beneficiaryBankCountryIso != null && !beneficiaryBankCountryIso.isEmpty())
            query.setParameter("beneficiaryBankCountryIso", beneficiaryBankCountryIso);

        List<ClearingTransfer> clearingTransferList = query.getResultList();
        TransferType transferType = getTransferType(TransferTypeEnum.Clearing.toString());

        List<ExternalTransferBO> externalTransferBOList = new ArrayList<ExternalTransferBO>();
        for (ClearingTransfer clearingTransfer : clearingTransferList)
            externalTransferBOList.add(new ExternalTransferBO(clearingTransfer, transferType));

        return externalTransferBOList;
        */
    }

    private List<RTGSTransferTypeBO> getRTGSTransferModeBOList(String username, String applic, String language) {
        String sql = "SELECT code, transferMode FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getRTGSTransferTypeList(?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(applic);
        params.add(language);


        return QueryUtils.executeNativeQuery(sql, RTGSTransferTypeBO.class, params, em);
    }

    /**
     * Private helper function return list of sepa transfers.
     * @param clientId
     * @param operationReference
     * @param startDate
     * @param endDate
     * @param periodCode
     * @param validityCode
     * @param beneficiaryName
     * @param beneficiaryCountryIso
     * @param beneficiaryBankBic
     * @return
     */

    private List<ExternalTransferBO> getSepaTransferList(String clientId, String operationReference, Date startDate, Date endDate, String periodCode,
                                                         String validityCode, String beneficiaryName, String beneficiaryCountryIso,
                                                         String beneficiaryBankBic) {
        return getSepaTransferList(clientId, operationReference, startDate, endDate, periodCode, validityCode, beneficiaryName, beneficiaryCountryIso,
                                   beneficiaryBankBic, null, null);

    }

    private List<ExternalTransferBO> getSepaTransferList(String clientId, String operationReference, Date startDate, Date endDate, String periodCode,
                                                         String validityCode, String beneficiaryName, String beneficiaryCountryIso,
                                                         String beneficiaryBankBic, String modeTransferCode, String serviceCode) {

        //Clearing Transfer
        String jpql =
            "Select o" + "   From SepaTransfer o" + "   Where o.debitAccount.client.client = :clientId " +
            "       And o.operationReference like CONCAT('%',:reference,'%') " +
            "       And o.periodicity.code = COALESCE (:periodCode, o.periodicity.code)" +
            "       And o.validity.code  = COALESCE( :validityCode, o.validity.code)" +
            "       And o.transferMode.code = COALESCE( :modeTransferCode, o.transferMode.code)" +
            "       And o.userServiceCode.code = COALESCE( :serviceCode, o.userServiceCode.code)";

        if (startDate != null && endDate != null)
            jpql += " And o.operationDate BETWEEN :startDate AND :endDate";

        if (!StringUtils.isEmpty(beneficiaryName))
            jpql += " And LOWER(o.beneficiaryName) like CONCAT( '%', LOWER(:beneficiaryName) , '%')";

        if (!StringUtils.isEmpty(beneficiaryCountryIso))
            jpql += " And o.beneficiaryBankCountry.isoCode2Chars = :beneficiaryCountryIso ";

        if (!StringUtils.isEmpty(beneficiaryBankBic))
            jpql += " And CONCAT(o.beneficiaryBankBic.bankBic,o.beneficiaryBankBic.branchCode) like CONCAT ( '%', :beneficiaryBankBic, '%' )";

        Query query = em.createQuery(jpql, SepaTransfer.class);
        query.setParameter("clientId", clientId);
        query.setParameter("reference", operationReference);
        query.setParameter("periodCode", periodCode);
        query.setParameter("validityCode", validityCode);

        if (startDate != null && endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
        }

        if (!StringUtils.isEmpty(beneficiaryCountryIso))
            query.setParameter("beneficiaryCountryIso", beneficiaryBankBic);

        if (!StringUtils.isEmpty(beneficiaryName))
            query.setParameter("beneficiaryName", beneficiaryName);

        if (!StringUtils.isEmpty(beneficiaryBankBic))
            query.setParameter("beneficiaryBankBic", beneficiaryBankBic);

        List<SepaTransfer> sepaTransferList = query.getResultList();
        TransferType transferType = getTransferType("VIRSEPA");

        List<ExternalTransferBO> externalTransferBOList = new ArrayList<ExternalTransferBO>();
        for (SepaTransfer sepaTransfer : sepaTransferList)
            externalTransferBOList.add(new ExternalTransferBO(sepaTransfer, transferType));

        return externalTransferBOList;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private List<BankBic> getBankBicNationFilteredSearch(String genericFilter, String countryCode, String bankNameFilter, String bicCodeFilter,
                                                         String cityNameFilter, Boolean isSepaTransferActive, Boolean isKeySwiftFilter,
                                                         String bicNationFilter) {
        return paymentsTransferCommonModelSessionEJBLocal.getBankBicNationFilteredSearch(genericFilter, countryCode, bankNameFilter, bicCodeFilter,
                                                                                         cityNameFilter, isSepaTransferActive, isKeySwiftFilter,
                                                                                         bicNationFilter);
    }

    private void fillInitialSwiftContextBO(SwiftTransferBO swiftTransferBO, SwiftTransferContextBO ctx) {
        fillSwiftContextObjectInit(swiftTransferBO, ctx, null, true);
    }

    private void fillSwiftContextObjectInit(SwiftTransferBO initialTransfer, SwiftTransferContextBO ctx, List<GlobalCommissionBO> commissionList,
                                            boolean isTreasuryMode) {
        //step 1
        ctx.setOperationReference(initialTransfer.getOperationReference());

        if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
            ctx.setOperationDate(initialTransfer.getOperationDate());
            ctx.setCutoffDate(initialTransfer.getCutoffDate());
        }

        ctx.setBeneficiaryBankName(initialTransfer.getBeneficiaryBankName());
        ctx.setIbanControl(initialTransfer.getIbanControl());
        ctx.setResidenceCountryIso(initialTransfer.getResidenceCountryIso());
        ctx.setResidenceCountryName(initialTransfer.getResidenceCountryName());
        ctx.setAmount(initialTransfer.getCreditAmount());
        ctx.setCounterValueAmount(initialTransfer.getDebitAmount());
        ctx.setStrongCurrencyIsoCode(initialTransfer.getStrongCurrencyCode());
        ctx.setWeakCurrencyIsoCode(initialTransfer.getWeakCurrencyCode());
        ctx.setExchangeRate(initialTransfer.getExchangeRate());

        ctx.setFirstExecutionDate(initialTransfer.getNextExecutionDate());
        ctx.setFinalMaturityDate(initialTransfer.getFinalMaturityDate());

        if (initialTransfer.getDebitAccount() != null)
            ctx.setDebitAccount(findAccountByCode(initialTransfer.getDebitAccount()));

        ctx.setAccount50k(initialTransfer.getAccount50k());

        if (initialTransfer.getCreditCurrencyCode() != null)
            ctx.setTransferCurrency(findCurrencyByCode(initialTransfer.getCreditCurrencyCode()));

        if (!PaymentsExternalTransferModelConstants.DUPLICATE.equals(ctx.getWizardMode()))
            ctx.setExchangeReferenceBO(new ExchangeReferenceBO(initialTransfer.getExchangeReference()));

        //step 2
        Client debitClient = findClientById(initialTransfer.getDebitClient());
        if (debitClient != null) {
            ctx.setClient(debitClient);
            ctx.setDebitClientBO(new DetailedClientBO(debitClient.getClient()));
            ctx.setDebitClientId(debitClient.getClient());
            ctx.setDebitClientName(debitClient.getName());
        }
        ctx.setOrderingPartyName1(initialTransfer.getOrderingPartyName());
        ctx.setOrderingPartyName2(initialTransfer.getOrderingPartyName2());
        ctx.setOrderingPartyAddress1(initialTransfer.getOrderingPartyAddress1());
        ctx.setOrderingPartyAddress2(initialTransfer.getOrderingPartyAddress2());
        ctx.setOrderingPartyPostalCode(initialTransfer.getOrderingPartyPostalCode());
        ctx.setBuySellCode(initialTransfer.getBuySellCode());

        //setReferenceCurrencyCode() TODO MODIF no info is being saved
        ctx.setMotive(StringUtils.concatenate(initialTransfer.getMotive1(), initialTransfer.getMotive2()));

        if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode())) {
            ctx.setDebitValueDate(initialTransfer.getDebitValueDate());
            ctx.setClientRelativeReference(initialTransfer.getRelativeReference());
            ctx.setChequeNumber(initialTransfer.getChequeNumber());
            ctx.setOrderingPartyReference(initialTransfer.getOrderingPartyReference());
        }

        //ordering party bankfil
        if (initialTransfer.getOrderingPartyBic() != null)
            ctx.setOrderingPartyBankfil(findBankBicByBic(initialTransfer.getOrderingPartyBic()));

        //Step 3
        // protected Boolean isReportPaymentActive; TODO mofif
        //protected String reportPaymentTypeCode; TODO modif
        if (PaymentsExternalTransferModelConstants.MODIFY.equals(ctx.getWizardMode()))
            ctx.setCreditValueDate(initialTransfer.getCreditValueDate());
        //Other fields related to the correspondent will be retireved in next 3 step if needed. To be checked with Mazen TODO

        Correspondent correspondent =
            getCorrespondent(initialTransfer.getCreditAccount(), initialTransfer.getCorrespondentCountryIsoCode(), initialTransfer.getTransferMode(),
                             ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(), ctx.getUserLanguage());
        if (correspondent == null)
            ctx.getErrorResponse().addWarningText(translate("INFO_UNABLE_TO_FIND_CORRESP", ctx.getUserLanguage()));
        else {
            ctx.setCorrespondent(correspondent);
            ctx.setCorrespondantBic(initialTransfer.getBicncr());
        }

        /****************************** CRP Step ******************************/
        PaymentReportingContextBO paymentReportingCtx = new PaymentReportingContextBO();

        EconomicCodeBO economicCodeBO = new EconomicCodeBO(initialTransfer.getPaymentEconomicCode());
        economicCodeBO.setDescription(initialTransfer.getPaymentEconomicLib());
        paymentReportingCtx.setPaymentEconomicCodeBO(economicCodeBO);

        PaymentReportingCodeBO paymentReportingCodeBO = new PaymentReportingCodeBO(initialTransfer.getPaymentReportingCode());
        paymentReportingCodeBO.setDescription(initialTransfer.getPaymentReportingLib());
        paymentReportingCtx.setPaymentReportingCodeBO(paymentReportingCodeBO);

        CountryBO countryBO = new CountryBO(initialTransfer.getPaymentCountry());
        countryBO.setCountryName(initialTransfer.getPaymentCountryLib());
        paymentReportingCtx.setPaymentCountryBO(countryBO);

        ResidenceTypeCodeBO residenceTypeCodeBO = new ResidenceTypeCodeBO(initialTransfer.getPaymentResidence());
        residenceTypeCodeBO.setDescription(initialTransfer.getPaymentResidenceLib());
        paymentReportingCtx.setPaymentResidenceTypeCodeBO(residenceTypeCodeBO);

        paymentReportingCtx.setPaymentAccountTypeCode(initialTransfer.getPaymentAccountType());
        ctx.setPaymentReportingContextBO(paymentReportingCtx);
        /****************************** CRP Step ******************************/

        //commission
        ctx.setCommissionList(commissionList);

        //LK step 3 but to move to swift in coordination with mazen: TODO
        ctx.setBeneficiaryCorrespondentAccount(initialTransfer.getBenefCorrespondentAccount());
        ctx.setBeneficiaryCorrespondentBankName(initialTransfer.getBenefCorrespondentBankName());
        ctx.setIntermediaryBankName(initialTransfer.getIntermediaryBankName());
        //End External

        //now swift
        ctx.setIsMT103("O".equals(initialTransfer.getMt103PlusFlag()));

        ctx.setBeneficiaryIban(initialTransfer.getBeneficiaryAccountIban());
        ctx.setBeneficiaryName(initialTransfer.getBeneficiaryName());
        ctx.setBeneficiaryAddress(initialTransfer.getBeneficiaryAddress());
        if (initialTransfer.getBeneficiaryBic() != null)
            ctx.setBeneficiaryBic(findBankBicByBic(initialTransfer.getBeneficiaryBic()));

        if (initialTransfer.getBeneficiaryBankAccount() != null) {
            String beneficiaryBankAccount = initialTransfer.getBeneficiaryBankAccount();
            if (beneficiaryBankAccount.indexOf("/") > -1) {
                String beneficiaryBankAccountSwiftAbrString = StringUtils.substringSafely(beneficiaryBankAccount, 0, 3);
                SwiftAbbreviation beneficiaryBankAccountAbr = getSwiftAbrByNameAndAbr("57", beneficiaryBankAccountSwiftAbrString, em);

                ctx.setBeneficiaryBankAccountSwiftAbr(beneficiaryBankAccountAbr);
                beneficiaryBankAccount = StringUtils.substringSafely(beneficiaryBankAccount, 3);
            }

            ctx.setBeneficiaryBankAccount(beneficiaryBankAccount);

        }

        //        ctx.setBeneficiaryBankAccount(initialTransfer.getBeneficiaryBankAccount());
        ctx.setBeneficiaryBankBranch(initialTransfer.getBeneficiaryBankBranch());

        if (initialTransfer.getCorrespondentBankBic() != null)
            ctx.setBeneficiaryCorrespondentBic(findBankBicByBic(initialTransfer.getCorrespondentBankBic()));

        if (initialTransfer.getIntermediaryBankBic() != null)
            ctx.setIntermediaryBic(findBankBicByBic(initialTransfer.getIntermediaryBankBic()));

        if (initialTransfer.getInfoToBeneficiaryBank() != null) {
            String intoToBenefBankSwiftAbrText = initialTransfer.getInfoToBeneficiaryBank();
            int secondSlashIndex = StringUtils.getOrdinalIndexOf(intoToBenefBankSwiftAbrText, "/", 2);
            if (secondSlashIndex != -1) {
                String infoToBenefBankSwiftAbrString = StringUtils.substringSafely(intoToBenefBankSwiftAbrText, 0, secondSlashIndex + 1);
                SwiftAbbreviation infoToBenefBankAbr = getSwiftAbrByNameAndAbr("72", infoToBenefBankSwiftAbrString, em);
                ctx.setInfoToBeneficiaryBankSwiftAbr(infoToBenefBankAbr);
                intoToBenefBankSwiftAbrText = StringUtils.substringSafely(intoToBenefBankSwiftAbrText, secondSlashIndex + 1);
                ctx.setInfoToBeneficiaryBankText(intoToBenefBankSwiftAbrText);
            }
        }

        //step 2
        if (initialTransfer.getPeriode() != null)
            ctx.setSwiftPeriodicity(getSwiftTransferPeriodicity(initialTransfer.getPeriode()));

        if (initialTransfer.getOrderingPartyAdviceLanguage() != null)
            ctx.setSwiftAdviceLanguage(getSwiftAdviceLanguage(initialTransfer.getOrderingPartyAdviceLanguage()));

        // treasury is not used with mode T only anymore, so we determine it from which get generate were calling based on a boolean variable
        if (initialTransfer.getTransferMode() != null) {
            if (isTreasuryMode) {
                List<SwiftTransferMode> treasuryTransferModeList = getTreasuryTransferModeList(null, null, null);
                if (treasuryTransferModeList != null && treasuryTransferModeList.size() > 0) {
                    for (SwiftTransferMode transferMode : treasuryTransferModeList) {
                        if (transferMode.getCode().equalsIgnoreCase(initialTransfer.getTransferMode())) {
                            ctx.setTransferMode(transferMode);
                            ctx.setSwiftTarget(transferMode.isSwiftTarget());
                            break;
                        }
                    }
                }

                ctx.setTransferModeCode(initialTransfer.getTransferMode());
            } else {
                ctx.setTransferMode(getSwiftTransferMode(null, initialTransfer.getTransferMode()));
            }
        }

        ctx.setOrderingPartyOnSwift("O".equals(initialTransfer.getOrderingPartyOnSwiftFlag()) ? "Y" : "N");
        ctx.setOrderingInstitution(initialTransfer.getOrderingPartyInstitution());
        ctx.setOrderingPartyBic(initialTransfer.getOrderingPartyBic());

        //step3
        if (initialTransfer.getBdlZone() != null)
            ctx.setBdlZone(findSwiftBDLZoneByCode(initialTransfer.getBdlZone()));
        ctx.setOurAccount(initialTransfer.getOurAccount());

        //List of siwft abbreviations
        if (initialTransfer.getBankOperationCode() != null)
            ctx.setBankOperationCode(getSwiftAbrByNameAndAbr("23", "B/" + initialTransfer.getBankOperationCode(), em));

        if (initialTransfer.getInstructionCode() != null)
            ctx.setInstructionCode(getSwiftAbrByNameAndAbr("23", initialTransfer.getInstructionCode(), em));

        if (initialTransfer.getInfoToCorrespondent() != null) {
            String infoToCorrespondentString = initialTransfer.getInfoToCorrespondent();
            int indexOf2ndSlash = StringUtils.getOrdinalIndexOf(infoToCorrespondentString, "/", 2);
            if (indexOf2ndSlash > 0) {
                infoToCorrespondentString = StringUtils.substringSafely(infoToCorrespondentString, 0, indexOf2ndSlash + 1);
                SwiftAbbreviation intoToCorrespondent = getSwiftAbrByNameAndAbr("72", infoToCorrespondentString, em);
                ctx.setInfoToCorrespondent(intoToCorrespondent);
                String infoToCorrespondentText = StringUtils.substringSafely(initialTransfer.getInfoToCorrespondent(), indexOf2ndSlash + 1);
                ctx.setInfoToCorrespondentText(infoToCorrespondentText);
            }
        }

        if (initialTransfer.getCharge() != null) {
            SwiftCharge swiftCharge = new SwiftCharge(initialTransfer.getCharge(), initialTransfer.getChargesDescription());
            ctx.setSwiftCharge(swiftCharge);
        }

        ctx.setInformation23(initialTransfer.getInformation23());

        //    setCorrespondantBic(initialTransfer.getCorrespondentBankBic())
        // private String correspondantBic;

        //    private String correspondentAccountTypeCode; TODO modif, i think this is for crp

        //commission  CHEKC ME WITH MARAM TODO: Maram stating it is still null
        ctx.setSwiftCommissionTypeCode(initialTransfer.getSwiftCommissionTypeCode());
        ctx.setInitialTransfer(initialTransfer);

        //LK adding fields for CTLX
        ctx.setBenefSendSwiftFlag(initialTransfer.getBenefCommCode());
        ctx.setBenefSendBicFlag(initialTransfer.getBenefKeySwift());

        ctx.setBenefBankSendSwiftFlag(initialTransfer.getBenefBankCommCode());
        ctx.setBenefBankSendBicFlag(initialTransfer.getBenefBankKeySwift());

        ctx.setBenefCorrespBankSendSwiftFlag(initialTransfer.getBenefCorrespCommCode());
        ctx.setBenefCorrespBankSendBicFlag(initialTransfer.getBenefCorrespKeySwift());

        ctx.setIntermediaryBankSendBicFlag(initialTransfer.getIntermediaryBankCommCode());

        ctx.setOurCorrespBankSendSwiftFlag(initialTransfer.getOurCorrespCommCode());
        ctx.setOurCorrespBankSendBicFlag(initialTransfer.getOurCorrespKeySwift());

        ctx.setOrderingSendSwiftFlag(initialTransfer.getOrderingPartyCommCode());
        ctx.setOrderingSendBicFlag(initialTransfer.getOrderingPartyKeySwift());

    }

    private void fillSwiftContextObjectFromReceivedSwift(SwiftTransferContextBO ctx, ReceivedSwiftTransferBO receivedSwiftTransferBO) {
        if (ctx != null && receivedSwiftTransferBO != null) {
            ctx.setOperationReference(receivedSwiftTransferBO.getOperationReference()); // nooper
            ctx.setInitialReference(receivedSwiftTransferBO.getInitialReference()); // nooperInit

            ctx.setOperationNature(receivedSwiftTransferBO.getOperationNature()); // natop

            ctx.setBeneficiaryIban(receivedSwiftTransferBO.getBeneficiaryAccountNumber()); // cptbe

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getBeneficiaryBic())) {
                ctx.setBeneficiaryBic(findBankBicByBic(receivedSwiftTransferBO.getBeneficiaryBic())); // bicbe
            }

            ctx.setBeneficiaryAddress(receivedSwiftTransferBO.getBeneficiaryAddress()); // adrbe
            ctx.setBeneficiaryBankAccount(receivedSwiftTransferBO.getBeneficiaryBankAccountNumber()); // cptbqbe
            ctx.setBenefSendBicFlag(receivedSwiftTransferBO.getBeneficiaryCommunicationType()); // ctlxbe
            ctx.setBenefBankSendBicFlag(receivedSwiftTransferBO.getKeySwiftFlag()); // ctlxbq
            ctx.setBeneficiaryName(receivedSwiftTransferBO.getBeneficiaryName()); // nombe
            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getCorrespBeneficiaryBankBic()))
                ctx.setBeneficiaryCorrespondentBic(findBankBicByBic(receivedSwiftTransferBO.getCorrespBeneficiaryBankBic())); // biccbqb

            ctx.setBeneficiaryCorrespondentAccount(receivedSwiftTransferBO.getCorrespBenefBankAccountNumber()); // cptcbqb

            ctx.setOperationDate(receivedSwiftTransferBO.getOperationDate());

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getTransferMode()))
                ctx.setTransferMode(getSwiftTransferMode(ctx.getReturnToSender(), receivedSwiftTransferBO.getTransferMode()));

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getPeriodicity()))
                ctx.setSwiftPeriodicity(getSwiftTransferPeriodicity(receivedSwiftTransferBO.getPeriodicity()));

            ctx.setDebitClientBO(new DetailedClientBO(receivedSwiftTransferBO.getDebitClient()));

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getDebitAccountNumber()))
                ctx.setDebitAccount(findAccountByCode(receivedSwiftTransferBO.getDebitAccountNumber()));

            ctx.setOrderingPartyBic(receivedSwiftTransferBO.getOrderingPartyBic());
            ctx.setOrderingSendBicFlag(!StringUtils.isEmpty(receivedSwiftTransferBO.getOrderingPartyBic()) ? "S" : null);

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getCreditCurrencyIsoCode()))
                ctx.setTransferCurrency(findCurrencyByCode(receivedSwiftTransferBO.getCreditCurrencyIsoCode()));

            ctx.setStrongCurrencyIsoCode(receivedSwiftTransferBO.getStrongCurrencyIsoCode());
            ctx.setExchangeRate(receivedSwiftTransferBO.getRate());
            ctx.setWeakCurrencyIsoCode(receivedSwiftTransferBO.getWeakCurrencyIsoCode());
            ctx.setAmount(receivedSwiftTransferBO.getCreditAmount());
            ctx.setCounterValueAmount(receivedSwiftTransferBO.getDebitAmount());
            ctx.setDebitValueDate(receivedSwiftTransferBO.getDebitValueDate());

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getOrderingPartyAdviceLanguage()))
                ctx.setSwiftAdviceLanguage(getSwiftAdviceLanguage(receivedSwiftTransferBO.getOrderingPartyAdviceLanguage()));

            ctx.setOrderingPartyName1(receivedSwiftTransferBO.getOrderingPartyName1()); // dordred1
            ctx.setOrderingPartyName2(receivedSwiftTransferBO.getOrderingPartyName2()); // dordred2
            ctx.setAccount50k(receivedSwiftTransferBO.getSwiftOrdPIban()); // cpt50k
            ctx.setOrderingPartyAddress1(receivedSwiftTransferBO.getOrderingPartyAddress1()); // adrdo1
            ctx.setOrderingPartyAddress2(receivedSwiftTransferBO.getOrderingPartyAddress2()); // adrdo2

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getRelativeReference()))
                ctx.setClientRelativeReference(receivedSwiftTransferBO.getRelativeReference()); // refrel
            else
                ctx.setClientRelativeReference(receivedSwiftTransferBO.getReference()); // ref

            ctx.setMotive(receivedSwiftTransferBO.getMotive1());

            if (!StringUtils.isEmpty(receivedSwiftTransferBO.getCreditAccountNumber())) {
                Correspondent correspondent =
                    getCorrespondent(receivedSwiftTransferBO.getCreditAccountNumber(), receivedSwiftTransferBO.getCreditCountryIso(),
                                     receivedSwiftTransferBO.getTransferMode(), ctx.getUser().getUsername(), TransferTypeEnum.Swift.toString(),
                                     ctx.getUserLanguage());

                if (correspondent == null)
                    ctx.getErrorResponse().addWarningText(PaymentsExternalTransferModelConstants.translate("INFO_UNABLE_TO_FIND_CORRESP",
                                                                                                           ctx.getUserLanguage()));
                else
                    ctx.setCorrespondent(correspondent);
            }

            ctx.setCreditValueDate(receivedSwiftTransferBO.getCreditValueDate());
            ctx.setCorrespondantBic(receivedSwiftTransferBO.getOurCorrespondantBic()); // bicncr
            ctx.setResidenceCountryIso(receivedSwiftTransferBO.getDebitCountryIso()); // isodo

            ctx.setInfoToBeneficiaryBankText(receivedSwiftTransferBO.getBeneficiaryBankMessage()); // mesbqbe
            ctx.setInfoToCorrespondentText(receivedSwiftTransferBO.getOurCorrespondentMessage()); // mesncr
        }
    }

    private void fillSwiftContextObjectFromInternetPayment(SwiftTransferContextBO ctx, InternetTransferBO internetPaymentTransferBO) {
        if (ctx != null && internetPaymentTransferBO != null) {
            ctx.setOperationReference(internetPaymentTransferBO.getOperationReference()); // nooper

            ctx.setBeneficiaryIban(internetPaymentTransferBO.getCreditAccount()); // cptbe

            //            if (!StringUtils.isEmpty(internetPaymentTransferBO.getBeneficiaryBankBic())) {
            //                ctx.setBeneficiaryBic(findBankBicByBic(internetPaymentTransferBO.getBeneficiaryBankBic())); // bicbe
            //            }

            ctx.setBeneficiaryAddress(internetPaymentTransferBO.getBeneficiaryAddress()); // adrbe
            ctx.setBeneficiaryName(internetPaymentTransferBO.getBeneficiaryName()); // nombe

            ctx.setOperationDate(internetPaymentTransferBO.getOperationDate());

            if (!StringUtils.isEmpty(internetPaymentTransferBO.getTransferMode()))
                ctx.setTransferMode(getSwiftTransferMode(null, internetPaymentTransferBO.getTransferMode()));

            ctx.setDebitClientBO(new DetailedClientBO(internetPaymentTransferBO.getClientId()));

            if (!StringUtils.isEmpty(internetPaymentTransferBO.getDebitAccount()))
                ctx.setDebitAccount(findAccountByCode(internetPaymentTransferBO.getDebitAccount()));

            if (!StringUtils.isEmpty(internetPaymentTransferBO.getCreditCurrency()))
                ctx.setTransferCurrency(findCurrencyByCode(internetPaymentTransferBO.getCreditCurrency()));

            ctx.setExchangeRate(internetPaymentTransferBO.getRate());

            ctx.setAmount(internetPaymentTransferBO.getCreditAmount());
            ctx.setCounterValueAmount(internetPaymentTransferBO.getDebitAmount());

            ctx.setMotive(internetPaymentTransferBO.getMotive());

            ctx.setFeesAccount(internetPaymentTransferBO.getFeesAccount());
            ctx.setFeesAccountValidFlag(internetPaymentTransferBO.getFeesAccountValidFlag());
            ctx.setPerception(internetPaymentTransferBO.getPerception());

            ctx.setFixedAmountCode(internetPaymentTransferBO.getFixedAmountCode());


            if ("D".equals(internetPaymentTransferBO.getFixedAmountCode())) {
                ctx.setReferenceCurrencyCode(PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_DEBIT_CODE);
            } else if ("C".equals(internetPaymentTransferBO.getFixedAmountCode())) {
                ctx.setReferenceCurrencyCode(PaymentsExternalTransferModelConstants.REFERENCE_CURRENCY_CREDIT_CODE);
            }

        }
    }

    /*
     * LK
     * This is a temporary code, it will be removed when SwiftTransfer is moved to the new approach
     * (it might not be  a good practice to define this method here) for a code that is permanent.
     */
    private SwiftAbbreviation getSwiftAbrByNameAndAbr(String field, String abbreviation, EntityManager em) {
        TypedQuery<SwiftAbbreviation> query = em.createNamedQuery("SwiftAbbreviation.findByFieldAndAbrWithLike", SwiftAbbreviation.class);
        query.setParameter("field", field).setParameter("abbreviation", abbreviation);

        List<SwiftAbbreviation> result = query.getResultList();
        return result != null && result.size() > 0 ? result.get(0) : null;
    }

    private Correspondent getCorrespondent(String accountNumber, String countryIsoCode, String operationMode, String username, String applic,
                                           String language) {
        String sql =
            "SELECT id, applic, bocio, bic, bicNom, bicUnpub, bicVille, xtypcion, compte, compteNom, " +
            "compteTyp, compteLornos, compteApplic, compteNcg, compteNcgActivite, compteCoddci, compteOplist, " +
            "compteClient, compteClientResid, compteClientLangue, compteClientPays, compteClientPaysNom, debcre, " +
            "devise, iso, paysNom, cles, datmaj, expl, mntseuil, seuil, cptcor, ordre, sys_created_by, " +
            "sys_created_date, sys_updated_by, sys_updated_date, sys_version_number, clasdatval " + "FROM TABLE(" + PLSQL_GLOBAL_PACKAGE_NAME +
            ".getCorrespondent(?, ?, ?, ?, ?, ?))";

        List<Object> params = new ArrayList<Object>();
        params.add(accountNumber);
        params.add(countryIsoCode);
        params.add(operationMode);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<Correspondent> correspondentList = QueryUtils.executeNativeQuery(sql, Correspondent.class, params, em);
        return correspondentList != null && !correspondentList.isEmpty() ? correspondentList.get(0) : null;
    }

    public List<SepaTransferCancellationBO> getSepaTransferCancellationBOList(String reference, String username, String applic, String language) {
        String sql =
            "Select prowid , reference, state, motif1, motif2, percep,agenceUtilisateur,agenceCompteDebit,mntdevc,mntdevd,devisec,devised" +
            ",comptec,compted,dev1,dev2,datoper,debitclient,creditClient,debitncg,creditncg,debitapplic" +
            ",creditapplic,debitoplist,creditoplist,modev, periode, typcptd from table(" + PLSQL_GLOBAL_PACKAGE_NAME +
            ".getSepaCancellation(?,?,?,?))";

        List<Object> params = new ArrayList<Object>();
        params.add(reference);
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SepaTransferCancellationBO> sepaCancellationList = QueryUtils.executeNativeQuery(sql, SepaTransferCancellationBO.class, params, em);

        return sepaCancellationList;

    }

    public SepaTransferCancellationBO getSepaTransferCancellationBO(String reference, String user, String applic, String language) {

        List<GlobalCommissionBO> commissions;
        SepaTransferCancellationBO sepaTransferCancellationBO = null;
        if (reference != null) {
            List<SepaTransferCancellationBO> sepaCancellationList = new ArrayList<SepaTransferCancellationBO>();
            sepaCancellationList = getSepaTransferCancellationBOList(reference, user, applic, language);
            if (!sepaCancellationList.isEmpty()) {
                sepaTransferCancellationBO = sepaCancellationList.get(0);
                if (sepaTransferCancellationBO.getReference() != null) {
                    commissions =
                        commissionSessionEJB.getOperationCommissionList(sepaTransferCancellationBO.getReference(),
                                                                        sepaTransferCancellationBO.getDebitAmount(),
                                                                        sepaTransferCancellationBO.getCreditAmount(),
                                                                        sepaTransferCancellationBO.getDebitAccount(),
                                                                        sepaTransferCancellationBO.getCreditAccount(), user, "VIRSEP", language);
                    if (commissions != null)
                        sepaTransferCancellationBO.setCommissions(commissions);
                }
                return sepaTransferCancellationBO;
            }
        }
        return new SepaTransferCancellationBO();
    }

    public List<EnumLovItem> getVirsepaStateEnumList(String language) {
        ResourceBundle bundle = ModelUtils.getBundle(PaymentsExternalTransferModelConstants.RESOURCE_BUNDLE_NAME, new Locale(language));
        return EnumUtils.enumValues(VirsepaStateEnum.class, bundle);
    }

    public List<SepaMotiveBO> getSepaMotiveBOList(String username, String applic, String language) {

        String sql = "Select code  , libelle description from table(" + PLSQL_GLOBAL_PACKAGE_NAME + ".getMotifCancellation(?,?,?))";

        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(applic);
        params.add(language);

        List<SepaMotiveBO> sepaMotiveList = QueryUtils.executeNativeQuery(sql, SepaMotiveBO.class, params, em);
        return sepaMotiveList;

    }

    public SaveSepaTransferCancellationResponse getSaveSepaTransferCancellation(SepaTransferCancellationBO sepaTransferCancellationBOCtx,
                                                                                List<GlobalCommissionBO> commisionsDetailList, String sessionPrinter,
                                                                                String user, String applic,
                                                                                String language) throws InstantiationException,
                                                                                                        IllegalAccessException, NoSuchFieldException,
                                                                                                        InvocationTargetException,
                                                                                                        InvocationTargetException {

        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_reference", JDBCTypes.VARCHAR_TYPE, sepaTransferCancellationBOCtx.getReference()));
        params.add(new PlsqlInputParameter("p_i_state", JDBCTypes.VARCHAR_TYPE, sepaTransferCancellationBOCtx.getState()));
        params.add(new PlsqlInputParameter("p_i_motif1", JDBCTypes.VARCHAR_TYPE, sepaTransferCancellationBOCtx.getMotif1()));
        params.add(new PlsqlInputParameter("p_i_motif2", JDBCTypes.VARCHAR_TYPE, sepaTransferCancellationBOCtx.getMotif2()));
        params.add(new PlsqlInputParameter("p_i_commissionList", CommissionStruct.class, commisionsDetailList));
        params.add(new PlsqlInputParameter("p_i_sessionPrinter", JDBCTypes.VARCHAR_TYPE, sessionPrinter));
        params.add(new PlsqlInputParameter("p_i_user", JDBCTypes.VARCHAR_TYPE, user));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));

        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "ins_upd_sepaCancellation", params, em);

        SaveSepaTransferCancellationResponse resp = new SaveSepaTransferCancellationResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        return resp;
    }


    public GetCancelSepaCancellationResponse getGetCancelSepaCancellation(String reference, String sessionPrinter, String user, String applic,
                                                                          String language) {

        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_reference", JDBCTypes.VARCHAR_TYPE, reference));
        params.add(new PlsqlInputParameter("p_i_sessionPrinter", JDBCTypes.VARCHAR_TYPE, sessionPrinter));
        params.add(new PlsqlInputParameter("p_i_user", JDBCTypes.VARCHAR_TYPE, user));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "cancelSepaCancellation", params, em);

        GetCancelSepaCancellationResponse resp = new GetCancelSepaCancellationResponse();
        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));

        return resp;
    }

    public Boolean isFXYSwiftReferEnabled() {
        String sql = "SELECT Y1  FROM fx5y8 WHERE tname='SWIFT'  AND model= 'REFER'";

        Query query = em.createNativeQuery(sql);

        List<String> resultList = query.getResultList();
        return resultList != null && resultList.size() > 0 ? "O".equals(resultList.get(0)) : false;
    }

    private CheckSwiftVersionResponse getCheckSwiftVersionResponse(String operationReference, BigDecimal swiftVersionNumber, String username,
                                                                   String applic, String language) {
        CheckSwiftVersionResponse resp = new CheckSwiftVersionResponse();
        List<PlsqlParameter> params = new ArrayList<PlsqlParameter>();
        params.add(new PlsqlInputParameter("p_i_nooper", JDBCTypes.VARCHAR_TYPE, operationReference));
        params.add(new PlsqlInputParameter("p_i_tableName", JDBCTypes.VARCHAR_TYPE, "swftrecus"));
        params.add(new PlsqlInputParameter("p_i_versionNumber", JDBCTypes.VARCHAR_TYPE, swiftVersionNumber));
        params.add(new PlsqlInputParameter("p_i_uname", JDBCTypes.VARCHAR_TYPE, username));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));

        HashMap<String, Object> returnParams = QueryUtils.executePLSQLScript("pk_capb_util", "validate_version_number", params, em);

        resp.setFnReturn((String) returnParams.get("RESULT"));
        resp.setErrorText((String) returnParams.get("p_o_errtxt"));
        return resp;
    }

    public Date getLastClearingDate() {
        TypedQuery<BankState> typedQuery = em.createNamedQuery("BankState.findAll", BankState.class);
        BankState bankState = typedQuery != null ? typedQuery.getSingleResult() : null;
        return bankState != null ? bankState.getLastClearingDate() : null;
    }
    // johnny 23/04/2018

    public ValidateInternetTrsfCommAmntInRangeResponse validateInternetTrsfCommAmntInRangeExternal(String nooper,
                                                                                                   List<GlobalCommissionBO> commissionList,
                                                                                                   String username, String applic,
                                                                                                   String language) throws InstantiationException,
                                                                                                                           IllegalAccessException,
                                                                                                                           NoSuchFieldException,
                                                                                                                           InvocationTargetException {

        return paymentsTransferCommonModelSessionEJBLocal.validateInternetTrsfCommAmntInRange(nooper, commissionList, applic, username, language);

    }

    private CheckUserAtHeadOfficeResponse checkUserAtHeadOffice(String reference, String username, String applic, String language) {
        List<PlsqlParameter> params = new ArrayList<>();
        params.add(new PlsqlInputParameter("p_i_reference", JDBCTypes.VARCHAR_TYPE, reference));
        params.add(new PlsqlInputParameter("p_i_user", JDBCTypes.VARCHAR_TYPE, username));
        params.add(new PlsqlInputParameter("p_i_applic", JDBCTypes.VARCHAR_TYPE, applic));
        params.add(new PlsqlInputParameter("p_i_lng", JDBCTypes.VARCHAR_TYPE, language));
        params.add(new PlsqlOutputParameter("p_o_isHeadOffice", JDBCTypes.VARCHAR_TYPE));
        params.add(new PlsqlOutputParameter("p_o_errtxt", JDBCTypes.VARCHAR_TYPE));
        Map<String, Object> execQuery = QueryUtils.executePLSQLScript(PLSQL_GLOBAL_PACKAGE_NAME, "checkUserAtHeadOffice", params, em);
        CheckUserAtHeadOfficeResponse resp = new CheckUserAtHeadOfficeResponse();
        resp.setFnReturn((String) execQuery.get("Result"));
        resp.setIsHeadOffice((String) execQuery.get("p_o_isHeadOffice"));
        resp.setErrorText((String) execQuery.get("p_o_errtxt"));
        return resp;
    }
}
