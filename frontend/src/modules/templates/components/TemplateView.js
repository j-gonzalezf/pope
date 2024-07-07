import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import { BsFillPlusCircleFill } from "react-icons/bs";
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
                    <br />
                    <p className='text-white'>Todavía no hay plantillas</p>
                    <Button className="primary cycle" >
                        <BsFillPlusCircleFill className="plusIconStyle cycle" />
                        <span>
                            <b><FormattedMessage id="project.templates.addTemplate" /></b>
                        </span>
                    </Button>
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
