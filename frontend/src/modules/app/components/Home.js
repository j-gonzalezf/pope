import { Container, Row, Col } from 'react-bootstrap';
import './Home.css';

import { FormattedMessage } from 'react-intl';
import TrainerIcon from '../../common/images/trainer-logo.webp';
import ClientIcon from '../../common/images/client-logo.webp';

const Home = () => {

  return (

    <Container>
      <Row className='home'>
        <Col xs={12} md={6} className='trainer-col'>
          <div className='trainer-content'>
            <img className='trainer-icon' src={TrainerIcon} alt="Trainer icon" />
            <b><FormattedMessage id="project.home.login" />
              <br /><FormattedMessage id="project.users.trainer" /></b>
          </div>
        </Col>
        <Col xs={12} md={6} className='client-col'>
          <div className='client-content'>
            <img className='client-icon' src={ClientIcon} alt="Client icon" />
            <b><FormattedMessage id="project.home.login" />
              <br /><FormattedMessage id="project.users.client" /></b>
          </div>
        </Col>
      </Row>
    </Container>

  );

}

export default Home;
