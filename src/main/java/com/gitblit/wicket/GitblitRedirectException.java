/*
 * Copyright 2012 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitblit.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.gitblit.utils.GitBlitRequestUtils;

/**
 * This exception bypasses the servlet container rewriting relative redirect
 * urls.  The container can and does decode the carefully crafted %2F path
 * separators on a redirect.  :(  Bad, bad servlet container.
 *
 * org.eclipse.jetty.server.Response#L447: String path=uri.getDecodedPath();
 *
 * @author James Moger
 */
public class GitblitRedirectException extends RestartResponseException {

	private static final long serialVersionUID = 1L;

	public <C extends Page> GitblitRedirectException(Class<C> pageClass) {
		this(pageClass, null);
	}

	public <C extends Page> GitblitRedirectException(Class<C> pageClass, PageParameters params) {
		super(pageClass, params);
		RequestCycle cycle = RequestCycle.get();
		String absoluteUrl = GitBlitRequestUtils.toAbsoluteUrl(pageClass,params);
		cycle.scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(absoluteUrl));	
	}
}
