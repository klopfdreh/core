/*
 * Copyright 2012 Igor Vaynberg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.wicketstuff.select2;

import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.Strings;
import org.wicketstuff.select2.json.JsonBuilder;

/**
 * Single-select Select2 component. Should be attached to a {@code <input type='hidden'/>} element.
 *
 * @param <T>
 *            type of choice object
 * @author igor
 */
public class Select2Choice<T> extends AbstractSelect2Choice<T, T>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 *
	 * @param id
	 */
	public Select2Choice(String id)
	{
		super(id);
	}

	/**
	 * Constructor.
	 *
	 * @param id
	 * @param model
	 */
	public Select2Choice(String id, IModel<T> model)
	{
		super(id, model);
	}

	/**
	 * Constructor.
	 *
	 * @param id
	 * @param model
	 * @param provider
	 */
	public Select2Choice(String id, IModel<T> model, ChoiceProvider<T> provider)
	{
		super(id, model, provider);
	}

	/**
	 * Constructor.
	 *
	 * @param id
	 * @param provider
	 */
	public Select2Choice(String id, ChoiceProvider<T> provider)
	{
		super(id, provider);
	}

	@Override
	protected final T convertValue(String[] value) throws ConversionException
	{
		if (value != null && value.length > 0 && !Strings.isEmpty(value[0]))
		{
			Iterator<T> it = convertIdsToChoices(Collections.singletonList(value[0])).iterator();
			return it.hasNext() ? it.next() : null;
		}
		else
		{
			return null;
		}
	}

	@Override
	protected void renderInitializationScript(IHeaderResponse response, T choice)
	{
		JsonBuilder selection = new JsonBuilder();
		try
		{
			selection.object();
			getProvider().toJson(choice, selection);
			selection.endObject();
		}
		catch (JSONException e)
		{
			throw new RuntimeException("Error converting model object to Json", e);
		}
		response.render(OnDomReadyHeaderItem.forScript(JQuery.execute(
			"$('#%s').select2('data', %s);", getJquerySafeMarkupId(), selection.toJson())));
	}
}
