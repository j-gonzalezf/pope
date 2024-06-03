import Col from 'react-bootstrap/Col';
import Image from 'react-bootstrap/Image';
import Row from 'react-bootstrap/Row';
import './Home.css';

import { FormattedMessage } from 'react-intl';
import { useNavigate } from 'react-router-dom';

import PopeLogo from '../../common/images/pope-logo.webp';
import ClientIcon from '../../common/images/dumbbells.webp';
import TrainerIcon from '../../common/images/captain.webp';

const Home = () => {

  const navigate = useNavigate();

  const redirectToLogin = (userType) => {
    navigate(`/users/login/${userType}`);
  };

  return (

    <div>
      <Row className='home'>
        {/* Columna para pope-col */}
        <Col xs={12} sm={12} md={6} className='pope-col'>
          <Image className='pope-logo' src={PopeLogo} alt="Pope logo" />
        </Col>
        <Col xs={12} sm={12} md={6} className='login-col'>
          <Row className='upper-right-quadrant'>
            {/* Columna para el entrenador */}
            <Col md={12} className='trainer-col' onClick={() => redirectToLogin('trainer')}>
              <div className='centered-content'>
                <Image className='trainer-icon' src={TrainerIcon} alt="Trainer icon" />
                <b><FormattedMessage id="project.home.login" />
                  <br /><FormattedMessage id="project.users.trainer" /></b>
              </div>
            </Col>
          </Row>
          <Row className='lower-right-quadrant'>
            {/* Columna para el cliente */}
            <Col md={12} className='client-col' onClick={() => redirectToLogin('client')}>
              <div className='centered-content'>
                <Image className='client-icon' src={ClientIcon} alt="Client icon" />
                <b><FormattedMessage id="project.home.login" />
                  <br /><FormattedMessage id="project.users.client" /></b>
              </div>
            </Col>
          </Row>
        </Col>
      </Row>
    </div>

  );

}

export default Home;
