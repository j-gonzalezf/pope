import React from 'react';
import { FormattedMessage } from 'react-intl';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { Chart } from 'react-charts';

import * as selectors from '../selectors';
import './GraphPage.css';

const GraphPage = () => {

    const sensations = useSelector(selectors.getSensations);

    const { sensationKey } = useParams();

    const filteredData = sensations.map(sensation => {
        const date = new Date(sensation.sensationDate);
        const formattedDate = date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
        return {
            primary: formattedDate,
            secondary: sensation[sensationKey],
        };
    });

    const lastSevenData = filteredData.slice(-7);

    const chartData = React.useMemo(
        () => [
            {
                label: `${sensationKey} Data`,
                data: lastSevenData,
            },
        ],
        [lastSevenData, sensationKey]
    );

    const axes = React.useMemo(
        () => [
            { primary: true, type: 'ordinal', position: 'bottom', tickCount: 1 },
            { type: 'linear', position: 'left', hardMin: 1, hardMax: 5, format: d => (d % 1 === 0 ? Math.floor(d) : '') },
        ],
        []
    );

    const getSeriesStyle = React.useCallback(
        () => ({
            stroke: '#e6af2e',
        }),
        []
    );

    const getDatumStyle = React.useCallback(
        () => ({
            r: 4,
            stroke: '#e6af2e',
            fill: 'white',
        }),
        []
    );

    return (
        <div fluid="true" className='GraphPage'>
            <h3 className="title">
                <FormattedMessage id={`project.tracking.${sensationKey}`} />
            </h3>
            <div className='GraphContainer'>
                <Chart
                    data={chartData}
                    axes={axes}
                    getSeriesStyle={getSeriesStyle}
                    getDatumStyle={getDatumStyle}
                    className="Graph"
                />
            </div>
        </div>
    );
};

export default GraphPage;
