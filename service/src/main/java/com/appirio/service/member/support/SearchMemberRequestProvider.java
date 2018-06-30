package com.appirio.service.member.support;

import com.appirio.service.member.api.MemberSearchRequest;
import com.appirio.tech.core.api.v3.request.annotation.APIQueryParam;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

/**
 * Search member request provider
 * @author TCSCODER
 */
@Provider
public class SearchMemberRequestProvider extends AbstractValueFactoryProvider{
    @Singleton
    public static final class MemberSearchRequestInjectionResolver
            extends ParamInjectionResolver<APIQueryParam>
    {
        public MemberSearchRequestInjectionResolver()
        {
            super(SearchMemberRequestProvider.class);
        }
    }

    public static final class MemberSearchRequestFactory
            extends AbstractContainerRequestValueFactory<MemberSearchRequest>
    {
        private Class<?> repClass;

        MemberSearchRequestFactory(Class<?> repClass)
        {
            this.repClass = repClass;
        }

        public MemberSearchRequest provide()
        {
            String limit = null;
            String offset = null;
            String handle = null;
            String query = null;
            String fields = null;
            String status = null;

            MultivaluedMap<String, String> params = getContainerRequest().getUriInfo().getQueryParameters();
            for (Map.Entry<String, List<String>> param : params.entrySet())
            {
                String key = param.getKey();
                String value = (String)((List)param.getValue()).iterator().next();
                if(key.equals("handle") && value != null) {
                    handle = value;
                } else if(key.equals("query") && value != null) {
                    query = value;
                } else if(key.equals("limit") && value != null) {
                    limit = value;
                } else if(key.equals("offset") && value != null) {
                    offset = value;
                } else if(key.equals("fields") && value != null) {
                    fields = value;
                } else if(key.equals("status") && value != null) {
                    status = value;
                }
            }
            MemberSearchRequest memberSearchRequest = new MemberSearchRequest();
            if(limit != null && !limit.isEmpty()) {
                memberSearchRequest.setLimit(Integer.valueOf(limit));
            }
            if(offset != null && !offset.isEmpty()) {
                memberSearchRequest.setOffset(Integer.valueOf(offset));
            }
            if(handle != null && !handle.isEmpty()) {
                memberSearchRequest.setHandle(handle);
            }
            if(query != null && !query.isEmpty()) {
                memberSearchRequest.setQuery(query);
            }
            if(fields != null && !fields.isEmpty()){
                memberSearchRequest.setFields(fields);
            }
            if(status != null && !status.isEmpty()){
                memberSearchRequest.setStatus(status);
            }
           return memberSearchRequest;
        }
    }

    @Inject
    protected SearchMemberRequestProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator)
    {
        super(mpep, locator, new Parameter.Source[] { Parameter.Source.UNKNOWN });
    }


    protected Factory<?> createValueFactory(Parameter parameter)
    {
        APIQueryParam annotation = parameter.getAnnotation(APIQueryParam.class);
        Class<?> paramType = parameter.getRawType();
        if ((annotation != null) && (paramType.isAssignableFrom(MemberSearchRequest.class))) {
            return new MemberSearchRequestFactory(annotation.repClass());
        }
        return null;
    }


    public static class Binder
            extends AbstractBinder
    {
        protected void configure()
        {
            bind(SearchMemberRequestProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(SearchMemberRequestProvider.MemberSearchRequestInjectionResolver.class)
                    .to(new TypeLiteral<InjectionResolver<APIQueryParam>>() {
                    })
                    .in(Singleton.class);
        }
    }
}
