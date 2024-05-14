import React from "react";
import { IntlProvider } from "react-intl";
import { Provider } from "react-redux";
import { HashRouter } from "react-router-dom";
import renderer from "react-test-renderer";
import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";

import { initReactIntl } from "../../../i18n";
import { Logout } from "../../../modules/users";
import { userMock } from "./User.mock";

const { locale, messages } = initReactIntl();

describe("Profile Logout", () => {
	/*
    const middlewares = [thunk];
	const mockStore = configureMockStore(middlewares);
	let store;

	store = mockStore(userMock);
    */
	it("renders correctly", () => {
        /*
		const tree = renderer.create(
			<Provider store={store}>
				<IntlProvider locale={locale} messages={messages}>
					<HashRouter>
						<Logout />
					</HashRouter>
				</IntlProvider>
			</Provider>
		).toJSON();
		expect(tree).toMatchSnapshot();
        */
	});

});
