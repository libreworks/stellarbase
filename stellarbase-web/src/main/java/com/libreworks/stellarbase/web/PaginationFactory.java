package com.libreworks.stellarbase.web;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.google.common.collect.ImmutableSet;
import com.libreworks.stellarbase.persistence.Pagination;
import com.libreworks.stellarbase.text.Patterns;
import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

public class PaginationFactory
{
	private static final String DESC = "desc";
	private static final String SORT = "sort";
	private static final String ORDER = "order";
	private static final String PAGE = "page";
	private static final String START_PAGE = "startPage";
	private static final String START_INDEX = "startIndex";
	private static final String START = "start";
	private static final String COUNT = "count";
	private static final String MAX = "max";
	private static final String LIMIT = "limit";
	private static final String OFFSET = "offset";
	private static final String HEADER_RANGE = "Range";

	private static final Pattern RANGE = Pattern.compile("^items=(\\d+)-(\\d+)$");
	private static final Pattern DOJO_SORT = Pattern.compile("^sort\\((.*)\\)$");
	private static final Set<String> MAX_ALIAS = ImmutableSet.of(MAX, LIMIT, COUNT);
	private static final Set<String> OFFSET_ALIAS = ImmutableSet.of(START, OFFSET);
	private static final Set<String> PAGE_ALIAS = ImmutableSet.of(PAGE, START_PAGE);
	
	private ObjectMapper objectMapper;
	private ArrayType extSortArrayType;
	
	public PaginationFactory()
	{
		this(new ObjectMapper());
	}
	
	public PaginationFactory(ObjectMapper objectMapper)
	{		
		this.objectMapper = Arguments.checkNull(objectMapper);
		this.extSortArrayType = objectMapper.getTypeFactory().constructArrayType(ExtSort.class);
	}
	
	public Pagination create(HttpServletRequest request)
	{
		return create(request, SORT);
	}
	
	public Pagination create(HttpServletRequest request, String sortParameter)
	{
		Integer offset = 0;
		Integer max = Integer.MAX_VALUE;
		
		@SuppressWarnings("unchecked")
		Set<String> params = ImmutableSet.copyOf(Collections.<String>list(request.getParameterNames()));

		// dojo.store.JsonRest - Range: items=0-24
		// Grails - &max=25&offset=0
		// dojox.data.QueryReadStore - &count=25&start=0
		// OpenSearch - &count=25&startIndex=1
		// OpenSearch w/ page - &count=25&startPage=1
		// Spring Data REST - &limit=25&page=1
		// ExtJS - &limit=25&start=0
		
		String range = request.getHeader(HEADER_RANGE);
		Matcher rm = range == null ? null : RANGE.matcher(range);
		if (rm != null && rm.matches()) {
			// dojo.store.JsonRest style
			offset = Integer.valueOf(rm.group(1));
			max = Integer.valueOf(rm.group(2)) - offset + 1;
		} else {
			Integer maxVal = parse(MAX_ALIAS, request, Integer.MAX_VALUE);
			if (Integer.MAX_VALUE != maxVal.intValue()) {
				max = maxVal;
			}
			// Grails, ExtJS, dojox.data.QueryReadStore, all zero-based
			Integer offVal = parse(OFFSET_ALIAS, request, 0);
			if (offVal > 0) {
				offset = offVal;
			}
			if (params.contains(START_INDEX) || params.contains(START_PAGE) || params.contains(PAGE)) {
			// OpenSearch style, 1-based
				Integer startIdx = parse(request.getParameter(START_INDEX), 0);
			// OpenSearch or Spring Data style, 1-based
				Integer startPage = parse(PAGE_ALIAS, request, 0);
				if (startIdx > 0) {
					offset = startIdx - 1;
				} else if (startPage > 0) {
					offset = (max * (startPage - 1));
				}
			}				
		}
		
		Map<String,Boolean> order = new LinkedHashMap<>();
		// formats:
		// Dojo - &sort(+foo,-bar)
		// Dojo w/field - &[field]=+foo,-bar
		// OpenSearchServer - &sort=foo&sort=-bar
		// OpenSearch extension - &sort=foo:ascending&sort=bar:descending
		// Grails - &sort=foo&order=asc
		// Spring Data REST - &sort=foo,asc&sort=bar,desc
		// ExtJS JSON - &sort=[{"property":"foo","direction":"asc"},{"property":"bar","direction":"desc"}]
		if (params.contains(sortParameter)) {
			if (params.contains(ORDER)) {
			// stupid Grails ordering
				order.put(request.getParameter(sortParameter),
					!DESC.equalsIgnoreCase(request.getParameter(ORDER)));
			} else {
				String[] values = request.getParameterValues(sortParameter);
				for (String s : values) {
					parseSort(s, order);
				}
			}
		} else {
			for (String s : params) {
				Matcher sm = DOJO_SORT.matcher(s);
				if (sm.matches()) {
					// +foo,-bar
					parseSort(sm.group(1), order);
				}
			}
		}
		
		return Pagination.create(max, offset, order);
	}
	
	private void parseSort(String sort, Map<String,Boolean> sorts)
	{
		if (StringUtils.isBlank(sort)) {
			return;
		}
		if (sort.startsWith("[")) {
			// it might be the ridiculous JSON ExtJS sort format
			try {
		        for(ExtSort s : objectMapper.<ExtSort[]>readValue(sort, extSortArrayType)) {
		        	sorts.put(s.getProperty(), !DESC.equalsIgnoreCase(s.getDirection()));
		        }
		        return;
			} catch (Exception e) {
				// No, it's not
			}
		}
		if (sort.endsWith(",asc")) {
		// foo,asc
			sorts.put(sort.substring(0, sort.length() - 4), Boolean.TRUE);
		} else if (sort.endsWith(",desc")) {
		// foo,desc
			sorts.put(sort.substring(0, sort.length() - 5), Boolean.FALSE);
		} else if (sort.endsWith(":ascending")) {
		// foo:ascending
			sorts.put(sort.substring(0, sort.length() - 10), Boolean.TRUE);
		} else if (sort.endsWith(":descending")) {
		// foo:descending
			sorts.put(sort.substring(0, sort.length() - 11), Boolean.FALSE);
		} else {
			for (String s : Patterns.COMMA.split(sort)) {
				if (s.startsWith(Strings.DASH)) {
				// -foo
					sorts.put(s.substring(1), Boolean.FALSE);
				} else if (s.startsWith(Strings.PLUS)) {
				// +foo
					sorts.put(s.substring(1), Boolean.TRUE);
				} else {
				// foo
					sorts.put(s, Boolean.TRUE);
				}
			}
		}
	}

	private static Integer parse(Set<String> names, HttpServletRequest request, Integer defaultValue)
	{
		for (String name : names) {
			String value = request.getParameter(name);
			if (NumberUtils.isDigits(value)) {
				return Integer.valueOf(value);
			}
		}
		return defaultValue;
	}
	
	private static Integer parse(String value, Integer defaultValue)
	{
		return NumberUtils.isDigits(value) ? Integer.valueOf(value) : defaultValue;
	}
	
	public static class ExtSort
	{
	    private String property;
	    private String direction;

	    public ExtSort()
	    {
	    }

	    public String getDirection()
	    {
	        return direction;
	    }

	    public String getProperty()
	    {
	        return property;
	    }

	    public void setDirection(String direction)
	    {
	        this.direction = direction;
	    }

	    public void setProperty(String property)
	    {
	        this.property = property;
	    }
	}	
}
