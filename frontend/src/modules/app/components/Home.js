import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Image from 'react-bootstrap/Image';
import Row from 'react-bootstrap/Row';
import './Home.css';

import { FormattedMessage } from 'react-intl';
import { useNavigate } from 'react-router-dom';

import ClientIcon from '../../common/images/client-logo.webp';
import TrainerIcon from '../../common/images/trainer-logo.webp';

const Home = () => {

  const navigate = useNavigate();

  const redirectToLogin = () => {
    navigate('/users/login');
  };

  return (

    <Container>
      <Row className='home'>
        <Col className='trainer-col' onClick={redirectToLogin}>
          <Col md="auto" className='trainer-content'>
            <Image className='trainer-icon' src={TrainerIcon} alt="Trainer icon" />
            <b><FormattedMessage id="project.home.login" />
              <br /><FormattedMessage id="project.users.trainer" /></b>
          </Col>
        </Col>
        <Col className='client-col'>
          <Col md="auto" className='client-content'>
            <Image className='client-icon' src={ClientIcon} alt="Client icon" />
            <b><FormattedMessage id="project.home.login" />
              <br /><FormattedMessage id="project.users.client" /></b>
          </Col>
        </Col>
      </Row>
    </Container>

  );

}

export default Home;
