import React from 'react'
import Prism from 'prismjs'

import styled from 'styled-components'
import 'prismjs/components/prism-java'
import '../../style/prism-material-light.css'

const Message = ({ isCode, text, language, thread }) => {
    if (isCode) {
        if (!language || !Prism.languages[language]) {
            return (
                <S.Wrap isCode={isCode}>
                    <S.Code>{text}</S.Code>
                </S.Wrap>
            )
        }

        const html = Prism.highlight(text, Prism.languages[language], language)
        return (
            <S.Wrap isCode={isCode}>
                <S.Code dangerouslySetInnerHTML={{ __html: html }} />
            </S.Wrap>
        )
    } else {
        return (
            <div>
                {thread.content.split('\n').map((line, idx) => (
                    <p key={idx}>{line}</p>
                ))}
            </div>
        )
    }
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.lightgray};
        background-color: #fafafa;
    `,
    Code: styled.pre`
        width: 100%;
        height: 100%;
        padding: 16px;
        border-radius: 8px;
        font-size: ${({ theme }) => theme.fontsize.sub1};

        & > code[class*='language-'] {
            height: 100%;
        }
    `,
}

export default Message
