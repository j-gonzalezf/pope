import React from 'react';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { HashRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';

import { initReactIntl } from '../../../i18n';
import Home from '../../../modules/app/components/Home';
import { homeMock } from './Home.mock';

const { locale, messages } = initReactIntl();

describe('Home Component', () => {
/*
  const middlewares = [thunk];
  const mockStore = configureMockStore(middlewares);
  let store = mockStore(homeMock);
*/
  it('renders correctly', () => {
    /*
    const tree = renderer.create(
      <Provider store={store}>
        <IntlProvider locale={locale} messages={messages}>
          <HashRouter>
            <Home />
          </HashRouter>
        </IntlProvider>
      </Provider>
    ).toJSON();
    expect(tree).toMatchSnapshot();
*/
  });

});
