import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import './TemplateView.css';

import { FormattedMessage } from 'react-intl';

const TemplateView = () => {

    return (

        <div fluid className='TemplateView'>

            <Row>

                <Col xs={12} sm={12} md={9} className='template'>
                    <h3 className="title templates">
                        <FormattedMessage id="project.templates.templateView.title" />
                    </h3>
                </Col>
                <Col xs={12} sm={12} md={3} className='comment'>
                    <h3 className="title comments">
                        Comentarios
                    </h3>
                </Col>

            </Row>

        </div >

    );

}

export default TemplateView;
