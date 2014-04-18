/*
    This file is part of Wicket-Contrib-TinyMce. See
    <http://http://wicketstuff.org/confluence/display/STUFFWIKI/wicket-contrib-tinymce>

    Wicket-Contrib-TinyMce is free software: you can redistribute it and/
    or modify it under the terms of the GNU Lesser General Public License
    as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Wicket-Contrib-TinyMce is distributed in the hope that it will be
    useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Wicket-Contrib-TinyMce.  If not, see
    <http://www.gnu.org/licenses/>.
 */
package wicket.contrib.tinymce;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Position;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;
import wicket.contrib.tinymce.settings.TinyMCESettings.Toolbar;
import wicket.contrib.tinymce.settings.WicketSavePlugin;

public class InPlaceEditComponent extends AbstractTextComponent
{
	private static final long serialVersionUID = 1L;
	private InPlaceSaveBehavior inPlaceSaveBehavior;
	private InPlaceEditBehavior inPlaceEditBehavior;
	private final Component triggerComponent;

	public InPlaceEditComponent(String id, IModel model)
	{
		super(id, model);
		this.triggerComponent = this;
	}

	public InPlaceEditComponent(String id, IModel model, Component triggerComponent)
	{
		super(id, model);
		this.triggerComponent = triggerComponent;
	}

	public InPlaceEditComponent(String id, String text)
	{
		super(id, new Model(text));
		this.triggerComponent = this;
	}

	public InPlaceEditComponent(String id, String text, Component triggerComponent)
	{
		super(id, new Model(text));
		this.triggerComponent = triggerComponent;
	}

	@Override
	protected void onInitialize() 
	{
		// TODO Auto-generated method stub
		super.onInitialize();
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
		// advanced theme required to add save/cancel buttons to toolbar
		inPlaceSaveBehavior = createSaveBehavior();
		if (inPlaceSaveBehavior != null)
		{
			add(inPlaceSaveBehavior);			
		}
		inPlaceEditBehavior = createEditBehavior(triggerComponent);
		if (inPlaceEditBehavior != null)
			add(inPlaceEditBehavior);
	}

	protected InPlaceEditBehavior createEditBehavior(Component triggerComponent)
	{
		TinyMCESettings settings = new TinyMCESettings(Theme.advanced);
		WicketSavePlugin savePlugin = new WicketSavePlugin(inPlaceSaveBehavior);
		
		return new InPlaceEditBehavior(settings, triggerComponent, savePlugin);
	}

	protected InPlaceSaveBehavior createSaveBehavior()
	{
		return new InPlaceSaveBehavior();
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		// the name tag is added by AbstractTextComponent, because it expects this
		// element to be an <input> tag. We don't need it, and it will render invalid
		// xhtml if this is not an input tag:
		tag.remove("name");
	}

	@Override
	public String getInputName()
	{
		return getMarkupId();
	}

	@Override
	public final void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		replaceComponentTagBody(markupStream, openTag, getValue());
	}	

	public InPlaceSaveBehavior getInPlaceSaveBehavior()
	{
		return inPlaceSaveBehavior;
	}

	public InPlaceEditBehavior getInPlaceEditBehavior()
	{
		return inPlaceEditBehavior;
	}
}
