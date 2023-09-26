import React from 'react'
import styled from 'styled-components'

const VideoBoard = () => {
    // const videoBoxes = ['권현수', '박종서', '서원호', '신승수', '최효빈', '황상미']
    const videoBoxes = ['권현수', '박종서', '서원호', '신승수', '최효빈']
    // const videoBoxes = ['권현수', '박종서', '서원호', '신승수', '', '', '', '', '', '']
    const { Wrap, ProfileName, VideoBox } = S

    return (
        <Wrap>
            {videoBoxes.map((name, index) => (
                <VideoBox key={index}>
                    <ProfileName>{name}</ProfileName>
                </VideoBox>
            ))}
        </Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        grid-template-rows: auto auto;
        grid-gap: 8px;
        justify-content: center;
        flex-wrap: wrap;
        background-color: ${({ theme }) => theme.color.background};
        margin: 0 16px 10px 8px;
        height: 100%;
        width: 100%;
        /* max-width: 1400px; */
        max-height: calc(100% - 96px);
        /*
        @media (min-width: 1200px) {
            grid-template-columns: repeat(3, 1fr);
        } */
    `,

    ProfileName: styled.div`
        font-size: 13px;
        margin-top: auto;
        padding: 4px;
    `,
    VideoBox: styled.div`
        width: 100%;
        height: 100%;
        /* min-width: 250px; */
        /* min-height: 150px; */
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        align-self: center;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        padding: 8px;
        margin: 8px;
    `,
}

export default VideoBoard
